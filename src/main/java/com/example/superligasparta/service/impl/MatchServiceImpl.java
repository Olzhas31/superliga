package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.TeamRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.UpdateMatchRequest;
import com.example.superligasparta.service.MatchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

  private final MatchRepository matchRepository;
  private final TeamRepository teamRepository;
  private final TournamentRepository tournamentRepository;
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;

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
  public Match getMatchById(Long id) {
    return matchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Match not found with id " + id));
  }

  @Override
  public Match createMatch(CreateMatchRequest request) {
    boolean tournamentExists = tournamentRepository.existsById(request.getTournamentId());
    if (!tournamentExists) {
      throw new EntityNotFoundException("Турнир с id " + request.getTournamentId() + " не найден");
    }

    validateTeamExistence(
        request.getHomeTeamId(),
        request.getAwayTeamId(),
        request.getTournamentId()
    );

    boolean alreadyPlayed = matchRepository.existsByRoundIdAndTeamId(request.getRoundId(), request.getHomeTeamId())
        || matchRepository.existsByRoundIdAndTeamId(request.getRoundId(), request.getAwayTeamId());

    if (alreadyPlayed) {
      throw new IllegalStateException("Команда уже играет матч в этом туре");
    }

    Match match = Match.builder()
        .homeTeamId(request.getHomeTeamId())
        .awayTeamId(request.getAwayTeamId())
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
    Match match = matchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Матч не найден"));
    boolean tournamentExists = tournamentRepository.existsById(request.getTournamentId());
    if (!tournamentExists) {
      throw new EntityNotFoundException("Турнир с id " + request.getTournamentId() + " не найден");
    }
    validateTeamExistence(
        request.getHomeTeamId(),
        request.getAwayTeamId(),
        request.getTournamentId()
    );

    boolean homeTeamConflict = matchRepository.existsByRoundIdAndTeamIdAndNotMatchId(
        request.getRoundId(), request.getHomeTeamId(), match.getId()
    );
    boolean awayTeamConflict = matchRepository.existsByRoundIdAndTeamIdAndNotMatchId(
        request.getRoundId(), request.getAwayTeamId(), match.getId()
    );

    if (homeTeamConflict || awayTeamConflict) {
      throw new IllegalStateException("Одна из команд уже участвует в другом матче этого тура");
    }

    match.setHomeTeamId(request.getHomeTeamId());
    match.setAwayTeamId(request.getAwayTeamId());
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
    Match match = matchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Матч с id " + id + " не найден"));
    matchRepository.delete(match);
  }

  private void validateTeamExistence(Long homeTeamId, Long awayTeamId, Long tournamentId) {
    boolean homeExists = teamRepository.existsById(homeTeamId);
    boolean awayExists = teamRepository.existsById(awayTeamId);

    if (!homeExists || !awayExists) {
      throw new EntityNotFoundException("Одна или обе команды не найдены");
    }

    boolean homeInTournament = tournamentTeamInfoRepository.existsByTournamentIdAndTeamId(tournamentId, homeTeamId);
    boolean awayInTournament = tournamentTeamInfoRepository.existsByTournamentIdAndTeamId(tournamentId, awayTeamId);

    if (!homeInTournament || !awayInTournament) {
      throw new IllegalArgumentException("Одна или обе команды не участвуют в турнире с id = " + tournamentId);
    }
  }


}

