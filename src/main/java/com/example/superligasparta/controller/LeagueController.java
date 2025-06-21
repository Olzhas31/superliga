package com.example.superligasparta.controller;

import com.example.superligasparta.model.LeagueTableRow;
import com.example.superligasparta.service.LeagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/league-table")
@RequiredArgsConstructor
@Tag(name = "League", description = "Работа с таблицей лиги")
public class LeagueController {

  private final LeagueService leagueService;

  @GetMapping("/{tournamentId}")
  @Operation(summary = "Получить таблицу лиги", description = "Расчетная таблица по сыгранным матчам")
  public List<LeagueTableRow> getTable(@PathVariable Long tournamentId) {
    return leagueService.getLeagueTable(tournamentId);
  }
}
