package com.example.superligasparta.controller.rest;

import com.example.superligasparta.model.stats.LeagueTableRow;
import com.example.superligasparta.model.stats.TeamFairPlayStatsDto;
import com.example.superligasparta.model.stats.TopScorerDto;
import com.example.superligasparta.service.TournamentStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments/{tournamentId}/stats")
@RequiredArgsConstructor
@Tag(name = "Tournament Stats", description = "Расчётная статистика по турниру")
public class TournamentStatsController {

  private final TournamentStatsService tournamentStatsService;

  @GetMapping("/league-table")
  @Operation(summary = "Получить таблицу лиги", description = "Расчетная таблица по сыгранным матчам")
  public List<LeagueTableRow> getLeagueTable(@PathVariable Long tournamentId) {
    return tournamentStatsService.getLeagueTable(tournamentId);
  }

  @GetMapping("/fair-play")
  @Operation(summary = "Fair Play таблица", description = "Количество карточек по командам в турнире")
  public List<TeamFairPlayStatsDto> getFairPlayTable(@PathVariable Long tournamentId) {
    return tournamentStatsService.getFairPlayStats(tournamentId);
  }

  @GetMapping("/scorers")
  @Operation(summary = "Список лучших бомбардиров", description = "На основе количества голов")
  public List<TopScorerDto> getTopScorers(@PathVariable Long tournamentId) {
    return tournamentStatsService.getScorers(tournamentId);
  }
}
