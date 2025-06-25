package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.Team;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.TeamRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.model.LeagueTableRow;
import com.example.superligasparta.service.LeagueService;
import com.example.superligasparta.util.LeagueTableRowBuilder;
import com.example.superligasparta.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {

  private final MatchRepository matchRepository;
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;
  private final TournamentRepository tournamentRepository;
  private final EntityValidator entityValidator;

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
}
