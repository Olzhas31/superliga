package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.MatchCard;
import com.example.superligasparta.domain.entity.MatchGoal;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchCardRepository;
import com.example.superligasparta.domain.repository.MatchGoalRepository;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.PlayerContractRepository;
import com.example.superligasparta.domain.repository.PlayerRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.model.enums.CardType;
import com.example.superligasparta.model.stats.LeagueTableRow;
import com.example.superligasparta.model.stats.TeamFairPlayStatsDto;
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
  private final MatchCardRepository matchCardRepository;

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

  @Override
  public List<TeamFairPlayStatsDto> getFairPlayStats(Long tournamentId) {
    // 1. Получаем всех участников турнира
    List<TournamentTeamInfo> participants = tournamentTeamInfoRepository.findByTournamentId(tournamentId);
    if (participants.isEmpty()) return List.of();

    // 2. Получаем ID матчей турнира
    List<Long> matchIds = matchRepository.findAllByTournamentId(tournamentId)
        .stream()
        .map(Match::getId)
        .toList();

    if (matchIds.isEmpty()) return List.of();

    // 3. Получаем все карточки
    List<MatchCard> cards = matchCardRepository.findAllByMatchIdIn(matchIds);

    // 4. Группируем карточки по участнику
    Map<Long, List<MatchCard>> cardsByParticipantId = new HashMap<>();

    for (MatchCard card : cards) {
      // Определяем, какой participant (tournament_team_info) связан с этим игроком
      playerContractRepository.findByPlayerIdAndTournamentTeamInfoIdIn(
              card.getPlayerId(),
              participants.stream().map(TournamentTeamInfo::getId).toList()
          ).stream()
          .findFirst()
          .ifPresent(contract -> {
            Long participantId = contract.getTournamentTeamInfoId();
            cardsByParticipantId.computeIfAbsent(participantId, k -> new ArrayList<>()).add(card);
          });
    }

    // 5. Собираем статистику по каждому участнику
    return participants.stream()
        .map(participant -> {
          List<MatchCard> teamCards = cardsByParticipantId.getOrDefault(participant.getId(), List.of());

          int yellow = (int) teamCards.stream().filter(c -> c.getCardType() == CardType.YELLOW).count();
          int red = (int) teamCards.stream().filter(c -> c.getCardType() == CardType.RED).count();

          return new TeamFairPlayStatsDto(
              participant.getTeamId(),
              participant.getDisplayName(),
              yellow,
              red,
              yellow + red
          );
        })
        .sorted(Comparator.comparingInt(TeamFairPlayStatsDto::getTotalCards))
        .toList();
  }
}
