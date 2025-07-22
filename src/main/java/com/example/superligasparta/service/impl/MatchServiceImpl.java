package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.mappers.MatchMapper;
import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.match.UpdateMatchRequest;
import com.example.superligasparta.service.MatchService;
import com.example.superligasparta.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
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
    return matchRepository.findById(id)
        .map(match -> {
          TournamentTeamInfo homeTeamInfo = tournamentTeamInfoRepository.findById(match.getHomeParticipantId()).get();
          TournamentTeamInfo awayTeamInfo = tournamentTeamInfoRepository.findById(match.getAwayParticipantId()).get();
          return MatchMapper.toDto(match, homeTeamInfo, awayTeamInfo);
        })
        .orElseThrow(() -> new EntityNotFoundException("Match not found with id " + id));
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
