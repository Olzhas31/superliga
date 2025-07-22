package com.example.superligasparta.controller.rest;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.match.UpdateMatchRequest;
import com.example.superligasparta.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Управление футбольными матчами")
@Validated
public class MatchApiController {

  private final MatchService matchService;

  @GetMapping
  @Operation(summary = "Получить все матчи")
  public List<Match> getAll() {
    return matchService.getAllMatches();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить матч по ID")
  public MatchDto get(@PathVariable Long id) {
    return matchService.getMatchById(id);
  }

  @PostMapping
  @Operation(summary = "Создать новый матч")
  public Match create(@RequestBody @Valid CreateMatchRequest request) {
    return matchService.createMatch(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить матч")
  public Match update(@PathVariable Long id, @RequestBody @Valid UpdateMatchRequest match) {
    return matchService.updateMatch(id, match);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить матч по ID")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    matchService.deleteMatch(id);
  }

  @GetMapping("/by-tournament/{tournamentId}")
  @Operation(summary = "Получить матчи по ID турнира")
  public List<Match> getMatchesByTournament(@PathVariable Long tournamentId) {
    return matchService.getMatchesByTournament(tournamentId);
  }
}
