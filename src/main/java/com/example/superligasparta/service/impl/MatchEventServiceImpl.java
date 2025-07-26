package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.MatchGoal;
import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.domain.repository.MatchCardRepository;
import com.example.superligasparta.domain.repository.MatchGoalRepository;
import com.example.superligasparta.model.enums.MatchEventType;
import com.example.superligasparta.model.matchGoal.MatchScore;
import com.example.superligasparta.service.MatchEventService;
import com.example.superligasparta.service.PlayerContractService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchEventServiceImpl implements MatchEventService {

  private final MatchCardRepository matchCardRepository;
  private final MatchGoalRepository matchGoalRepository;
  private final PlayerContractService playerContractService;

  @Override
  public void deleteByIdAndEventType(Long id, MatchEventType eventType) {
    switch (eventType.getCategory()) {
      case CARD -> matchCardRepository.deleteById(id);
      case GOAL -> matchGoalRepository.deleteById(id);
    }
  }

  @Override
  public MatchScore calculateScore(Match match) {
    List<MatchGoal> goals = matchGoalRepository.findAllByMatchId(match.getId());
    Integer homeGoals = 0;
    Integer awayGoals = 0;

    for (MatchGoal goal : goals) {
      // Дата матча нужна для определения актуального контракта
      LocalDateTime matchDate = match.getMatchDateTime();

      // Найдём контракт игрока, действующий на момент матча
      PlayerContract contract = playerContractService.findActiveContractForMatch(
              goal.getPlayerId(), match.getHomeParticipantId(), match.getAwayParticipantId(), matchDate.toLocalDate())
          .orElseThrow(() -> new IllegalStateException("Контракт игрока не найден"));

      Long teamId = contract.getTournamentTeamInfoId();
      boolean isHomePlayer = match.getHomeParticipantId().equals(teamId);

      if (goal.isOwnGoal()) {

        // Автогол — в пользу противоположной команды
        if (isHomePlayer) {
          awayGoals++;
        } else {
          homeGoals++;
        }
      } else {
        if (isHomePlayer) {
          homeGoals++;
        } else {
          awayGoals++;
        }
      }
    }

    return MatchScore.builder()
        .homeGoals(homeGoals)
        .awayGoals(awayGoals)
        .build();
  }

}
