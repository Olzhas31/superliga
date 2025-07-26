package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.MatchCard;
import com.example.superligasparta.domain.entity.MatchGoal;
import com.example.superligasparta.domain.entity.Player;
import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchCardRepository;
import com.example.superligasparta.domain.repository.MatchGoalRepository;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.PlayerRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.mappers.MatchMapper;
import com.example.superligasparta.model.enums.CardType;
import com.example.superligasparta.model.enums.MatchEventType;
import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.match.MatchEventDto;
import com.example.superligasparta.model.match.UpdateMatchRequest;
import com.example.superligasparta.model.matchGoal.MatchScore;
import com.example.superligasparta.service.MatchEventService;
import com.example.superligasparta.service.MatchService;
import com.example.superligasparta.service.PlayerContractService;
import com.example.superligasparta.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

  private final MatchRepository matchRepository;
  private final TournamentRepository tournamentRepository;
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;
  private final MatchGoalRepository matchGoalRepository;
  private final MatchCardRepository matchCardRepository;
  private final PlayerRepository playerRepository;
  private final PlayerContractService playerContractService;
  private final MatchEventService matchEventService;
  private final EntityValidator entityValidator;

  @Override
  public List<Match> getAllMatches() {
    return matchRepository.findAll();
  }

  @Override
  public List<Match> getMatchesByTournament(Long tournamentId) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new EntityNotFoundException("Турнир с id " + tournamentId + " не найден");
    }
    return matchRepository.findAllByTournamentId(tournamentId);
  }

  @Override
  public MatchDto getMatchById(Long id) {
    Match match = matchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Match not found with id " + id));

    TournamentTeamInfo homeTeamInfo = tournamentTeamInfoRepository.findById(match.getHomeParticipantId()).get();
    TournamentTeamInfo awayTeamInfo = tournamentTeamInfoRepository.findById(match.getAwayParticipantId()).get();
    LocalDate matchDate = match.getMatchDateTime().toLocalDate();

    // Голы
    List<MatchGoal> goals = matchGoalRepository.findAllByMatchId(id);
    List<MatchEventDto> goalEvents = goals.stream()
        .map(goal -> {
          Player player = playerRepository.findById(goal.getPlayerId())
              .orElseThrow(() -> new IllegalArgumentException("Player not found: " + goal.getPlayerId()));
          MatchEventType eventType = determineGoalEventType(goal);
          return buildMatchEvent(
              goal.getId(),
              player,
              match.getHomeParticipantId(),
              match.getAwayParticipantId(),
              matchDate,
              goal.getMinute(),
              eventType
          );
        }).toList();

    // Карточки
    List<MatchCard> cards = matchCardRepository.findAllByMatchId(id);
    List<MatchEventDto> cardEvents = cards.stream()
        .map(card -> {
          Player player = playerRepository.findById(card.getPlayerId())
              .orElseThrow(() -> new IllegalArgumentException("Player not found: " + card.getPlayerId()));
          MatchEventType eventType = mapCardTypeToEventType(card.getCardType());
          return buildMatchEvent(
              card.getId(),
              player,
              match.getHomeParticipantId(),
              match.getAwayParticipantId(),
              matchDate,
              card.getMinute(),
              eventType
          );
        })
        .toList();


    // Собираем и сортируем события
    List<MatchEventDto> allEvents = new ArrayList<>();
    allEvents.addAll(goalEvents);
    allEvents.addAll(cardEvents);
    allEvents.sort(Comparator.comparing(MatchEventDto::getMinute, Comparator.nullsFirst(Integer::compareTo)));

    MatchScore matchScore = matchEventService.calculateScore(match);

    // Создаём финальный DTO
    MatchDto dto = MatchMapper.toDto(match, homeTeamInfo, awayTeamInfo, matchScore);
    dto.setEvents(allEvents);
    return dto;
  }

  @Override
  public Match createMatch(CreateMatchRequest request) {
    entityValidator.validateTournamentExists(request.getTournamentId());
    validateParticipantExistence(
        request.getHomeParticipantId(),
        request.getAwayParticipantId(),
        request.getTournamentId()
    );

    boolean homeAlreadyPlayed = matchRepository.existsByRoundIdAndParticipantId(
        request.getRoundId(), request.getHomeParticipantId());

    boolean awayAlreadyPlayed = matchRepository.existsByRoundIdAndParticipantId(
        request.getRoundId(), request.getAwayParticipantId());

    if (homeAlreadyPlayed || awayAlreadyPlayed) {
      throw new IllegalStateException("Одна из команд уже играет матч в этом туре");
    }

    Match match = Match.builder()
        .homeParticipantId(request.getHomeParticipantId())
        .awayParticipantId(request.getAwayParticipantId())
//        .homeGoals(request.getHomeGoals())
//        .awayGoals(request.getAwayGoals())
        .matchDateTime(request.getMatchDateTime())
        .played(request.getPlayed())
        .tournamentId(request.getTournamentId())
        .roundId(request.getRoundId())
        .build();

    return matchRepository.save(match);
  }

  public Match updateMatch(Long id, UpdateMatchRequest request) {
    entityValidator.validateTournamentExists(request.getTournamentId());
    // TODO сделать валидатором
    Match match = matchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Матч не найден"));

    // TODO сделать валидатором
    validateParticipantExistence(
        request.getHomeParticipantId(),
        request.getAwayParticipantId(),
        request.getTournamentId()
    );

    boolean homeConflict = matchRepository.existsByRoundIdAndParticipantIdAndNotMatchId(
        request.getRoundId(), request.getHomeParticipantId(), match.getId()
    );

    boolean awayConflict = matchRepository.existsByRoundIdAndParticipantIdAndNotMatchId(
        request.getRoundId(), request.getAwayParticipantId(), match.getId()
    );

    if (homeConflict || awayConflict) {
      throw new IllegalStateException("Одна из команд уже участвует в другом матче этого тура");
    }

    match.setHomeParticipantId(request.getHomeParticipantId());
    match.setAwayParticipantId(request.getAwayParticipantId());
//    match.setHomeGoals(request.getHomeGoals());
//    match.setAwayGoals(request.getAwayGoals());
    match.setMatchDateTime(request.getMatchDateTime());
    match.setPlayed(request.getPlayed());
    match.setTournamentId(request.getTournamentId());
    match.setRoundId(request.getRoundId());

    return matchRepository.save(match);
  }


  @Override
  public void deleteMatch(Long id) {
    // TODO сделать валидатором
    Match match = matchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Матч с id " + id + " не найден"));
    matchRepository.delete(match);
  }

  @Override
  public void updateMatch(MatchDto newMatchDto) {
    Match match = matchRepository.findById(newMatchDto.getId()).get();
    match.setMatchDateTime(newMatchDto.getMatchDateTime());
    match.setPlayed(newMatchDto.getPlayed());

    matchRepository.save(match);
  }

  // TODO перемести в валидатор
  private void validateParticipantExistence(Long homeParticipantId, Long awayParticipantId, Long tournamentId) {
    Optional<TournamentTeamInfo> homeParticipant = tournamentTeamInfoRepository.findById(homeParticipantId);
    Optional<TournamentTeamInfo> awayParticipant = tournamentTeamInfoRepository.findById(awayParticipantId);

    if (homeParticipant.isEmpty() || awayParticipant.isEmpty()) {
      throw new EntityNotFoundException("Один или оба участника турнира не найдены");
    }

    if (!homeParticipant.get().getTournamentId().equals(tournamentId) ||
        !awayParticipant.get().getTournamentId().equals(tournamentId)) {
      throw new IllegalArgumentException("Один или оба участника не принадлежат турниру с id = " + tournamentId);
    }
  }

  private MatchEventDto buildMatchEvent(Long id,
      Player player,
      Long homeTeamId,
      Long awayTeamId,
      LocalDate matchDate,
      Integer minute,
      MatchEventType type) {
    PlayerContract contract = playerContractService.findActiveContractForMatch(
        player.getId(), homeTeamId, awayTeamId, matchDate
    ).orElseThrow(() -> new EntityNotFoundException("У игрока нет контракта по этой дате: " + matchDate));

    boolean isHome = contract.getTournamentTeamInfoId().equals(homeTeamId);

    return MatchEventDto.builder()
        .id(id)
        .minute(minute)
        .type(type)
        .playerName(player.getName())
        .homeTeam(isHome)
        .build();
  }

  private MatchEventType determineGoalEventType(MatchGoal goal) {
    if (goal.isOwnGoal()) {
      return MatchEventType.OWN_GOAL;
    } else if (goal.isPenalty()) {
      return MatchEventType.PENALTY;
    } else {
      return MatchEventType.GOAL;
    }
  }

  private MatchEventType mapCardTypeToEventType(CardType cardType) {
    return switch (cardType) {
      case YELLOW -> MatchEventType.YELLOW;
      case RED -> MatchEventType.RED;
    };
  }
}
