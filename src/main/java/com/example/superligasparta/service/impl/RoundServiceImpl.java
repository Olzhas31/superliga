package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Round;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.RoundRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.mappers.MatchMapper;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.matchGoal.MatchScore;
import com.example.superligasparta.model.round.CreateRoundRequest;
import com.example.superligasparta.model.round.RoundDto;
import com.example.superligasparta.service.MatchGoalService;
import com.example.superligasparta.service.RoundService;
import com.example.superligasparta.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

  private final RoundRepository roundRepository;
  private final TournamentRepository tournamentRepository;
  private final EntityValidator entityValidator;
  private final MatchRepository matchRepository;
  private final TournamentTeamInfoRepository teamInfoRepository;
  private final MatchGoalService matchGoalService;

  @Override
  public Round create(CreateRoundRequest request) {
    if (!tournamentRepository.existsById(request.getTournamentId())) {
      throw new EntityNotFoundException("Турнир с id " + request.getTournamentId() + " не найден");
    }

    boolean nameExists = roundRepository
        .existsByTournamentIdAndName(request.getTournamentId(), request.getName());

    if (nameExists) {
      throw new IllegalArgumentException("Тур с таким названием уже существует в этом турнире");
    }

    Round round = new Round();
    round.setName(request.getName());
    round.setTournamentId(request.getTournamentId());

    return roundRepository.save(round);
  }

  @Override
  public List<Round> getByTournament(Long tournamentId) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new EntityNotFoundException("Турнир с id " + tournamentId + " не найден");
    }
    return roundRepository.findAllByTournamentId(tournamentId);
  }

  @Override
  public List<RoundDto> getRoundsWithMatchesByTournamentId(Long tournamentId) {
    entityValidator.validateTournamentExists(tournamentId);
    return roundRepository.findAllByTournamentId(tournamentId)
        .stream()
        .map(round -> {
          List<MatchDto> matches = matchRepository.findAllByRoundId(round.getId())
              .stream().map(m-> {
                    TournamentTeamInfo homeTeamInfo = teamInfoRepository.findById(m.getHomeParticipantId()).get();
                    TournamentTeamInfo awayTeamInfo = teamInfoRepository.findById(m.getAwayParticipantId()).get();
                    MatchScore matchScore = null;
                    if (m.getPlayed()){
                      matchScore = matchGoalService.calculateScore(m);
                    }
                    return MatchMapper.toDto(m, homeTeamInfo, awayTeamInfo, matchScore);
                  }
              ).toList();
          RoundDto roundDto = new RoundDto();
          roundDto.setId(round.getId());
          roundDto.setName(round.getName());
          roundDto.setTournamentId(round.getTournamentId());
          roundDto.setMatches(matches);
          return roundDto;
        }).toList();
  }
}
