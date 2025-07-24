package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.MatchCard;
import com.example.superligasparta.domain.entity.MatchGoal;
import com.example.superligasparta.domain.entity.Player;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchCardRepository;
import com.example.superligasparta.domain.repository.MatchGoalRepository;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.PlayerRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.mappers.MatchMapper;
import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.match.MatchEventDto;
import com.example.superligasparta.model.match.UpdateMatchRequest;
import com.example.superligasparta.service.MatchService;
import com.example.superligasparta.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
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

    // Голы
    List<MatchGoal> goals = matchGoalRepository.findAllByMatchId(id);
    List<MatchEventDto> goalEvents = goals.stream().map(goal -> {
      Player player = playerRepository.findById(goal.getPlayerId()).orElseThrow();
//      boolean isHome = homeTeamInfo.getTeamId().equals(player.getCurrentTeamId());
      String type = goal.isOwnGoal() ? "OWN_GOAL" : (goal.isPenalty() ? "PENALTY" : "GOAL");
      return MatchEventDto.builder()
          .minute(goal.getMinute())
          .type(type)
          .playerName(player.getName())
//          .homeTeam(isHome)
          .build();
    }).toList();

    // TODO
    // Карточки
//    List<MatchCard> cards = matchCardRepository.findByMatchId(id);
//    List<MatchEventDto> cardEvents = cards.stream().map(card -> {
//      Player player = playerRepository.findById(card.getPlayerId()).orElseThrow();
//      boolean isHome = homeTeamInfo.getTeamId().equals(player.getCurrentTeamId());
//      return MatchEventDto.builder()
//          .minute(card.getMinute())
//          .type(card.getCardType().name()) // "YELLOW", "RED"
//          .playerName(player.getName())
//          .homeTeam(isHome)
//          .build();
//    }).toList();
    List<MatchEventDto> allEvents = new ArrayList<>();
    allEvents.addAll(goalEvents);
//    allEvents.addAll(cardEvents);

    allEvents.sort(Comparator.comparing(MatchEventDto::getMinute));

    MatchDto dto = MatchMapper.toDto(match, homeTeamInfo, awayTeamInfo);
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
        .homeGoals(request.getHomeGoals())
        .awayGoals(request.getAwayGoals())
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
    match.setHomeGoals(request.getHomeGoals());
    match.setAwayGoals(request.getAwayGoals());
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
}
