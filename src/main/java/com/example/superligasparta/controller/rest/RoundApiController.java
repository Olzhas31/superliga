package com.example.superligasparta.controller.rest;

import com.example.superligasparta.domain.entity.Round;
import com.example.superligasparta.model.round.CreateRoundRequest;
import com.example.superligasparta.service.RoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
@Tag(name = "Rounds", description = "Управление турами турнира")
public class RoundApiController {

  private final RoundService roundService;

  @PostMapping
  @Operation(summary = "Создать тур")
  public Round create(@RequestBody @Valid CreateRoundRequest request) {
    return roundService.create(request);
  }

  @GetMapping("/by-tournament/{tournamentId}")
  @Operation(summary = "Получить все туры турнира")
  public List<Round> getByTournament(@PathVariable Long tournamentId) {
    return roundService.getByTournament(tournamentId);
  }
}
