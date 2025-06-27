package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.MatchGoal;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchGoalRepository;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.PlayerContractRepository;
import com.example.superligasparta.domain.repository.PlayerRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.model.stats.LeagueTableRow;
import com.example.superligasparta.model.stats.TopScorerDto;
import com.example.superligasparta.service.TournamentStatsService;
import com.example.superligasparta.util.LeagueTableRowBuilder;
import com.example.superligasparta.validation.EntityValidator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TournamentStatsServiceImpl implements TournamentStatsService {

  private final MatchRepository matchRepository;
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;
  private final TournamentRepository tournamentRepository;
  private final EntityValidator entityValidator;
  private final MatchGoalRepository matchGoalRepository;
  private final PlayerContractRepository playerContractRepository;
  private final PlayerRepository playerRepository;

  @Override
  public List<LeagueTableRow> getLeagueTable(Long tournamentId) {
    entityValidator.validateTournamentExists(tournamentId);

    // 1. Получаем все сыгранные матчи турнира
    List<Match> matches = matchRepository.findAllByTournamentIdAndPlayedTrue(tournamentId);

    // 2. Получаем команды этого турнира из tournament_team_info
    List<TournamentTeamInfo> tournamentTeams = tournamentTeamInfoRepository.findByTournamentId(tournamentId);

    // 3. Формируем мапу команд для таблицы
    Map<Long, LeagueTableRowBuilder> rows = new HashMap<>();
    for (TournamentTeamInfo info : tournamentTeams) {
      rows.put(info.getTeamId(), new LeagueTableRowBuilder(info.getTeamId(), info.getDisplayName()));
    }

    // 4. Обрабатываем матчи
    for (Match match : matches) {
      LeagueTableRowBuilder home = rows.get(match.getHomeParticipantId());
      LeagueTableRowBuilder away = rows.get(match.getAwayParticipantId());

      if (home != null) home.addMatch(match.getHomeGoals(), match.getAwayGoals());
      if (away != null) away.addMatch(match.getAwayGoals(), match.getHomeGoals());
    }

    // 5. Собираем и сортируем результат
    List<LeagueTableRow> result = rows.values().stream()
        .map(LeagueTableRowBuilder::build)
        .toList();
    Comparator<LeagueTableRow> leagueTableComparator = Comparator
        .<LeagueTableRow>comparingInt(row -> row.points() * -1) // по убыванию очков
        .thenComparingInt(row -> row.goalDifference() * -1)     // по убыванию разницы мячей
        .thenComparingInt(row -> row.goalsFor() * -1)           // по убыванию голов
        .thenComparing(LeagueTableRow::teamName);               // по алфавиту (по возрастанию)
    return result.stream()
        .sorted(leagueTableComparator)
        .toList();
  }

  @Override
  public List<TopScorerDto> getScorers(Long tournamentId) {
    List<Match> matches = matchRepository.findAllByTournamentIdAndPlayedTrue(tournamentId);
    List<Long> matchIds = matches.stream().map(Match::getId).toList();

    if (matchIds.isEmpty()) return List.of();

    List<MatchGoal> goals = matchGoalRepository.findByMatchIds(matchIds);

    // Группировка по игроку
    Map<Long, List<MatchGoal>> byPlayer = goals.stream()
        .collect(Collectors.groupingBy(MatchGoal::getPlayerId));

    // Построение DTO
    return byPlayer.entrySet().stream()
        .map(entry -> {
          Long playerId = entry.getKey();
          List<MatchGoal> playerGoals = entry.getValue();

          int goalsScored = (int) playerGoals.stream().filter(g -> !g.isOwnGoal()).count();
          int ownGoals = (int) playerGoals.stream().filter(MatchGoal::isOwnGoal).count();
          int score = goalsScored - ownGoals;

          String playerName = playerRepository.findById(playerId)
              .map(p -> p.getName() + " " + p.getSurname())
              .orElse("Unknown");

          return TopScorerDto.builder()
              .playerId(playerId)
              .playerName(playerName)
              .score(score)
              .ownGoals(ownGoals)
              .goals(goalsScored)
              .build();
        })
        .sorted(Comparator.comparingInt(TopScorerDto::getScore).reversed())
        .toList();
  }
}
