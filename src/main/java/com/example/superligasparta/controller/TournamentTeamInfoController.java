package com.example.superligasparta.controller;

import com.example.superligasparta.model.tournamentTeam.CreateTournamentTeamInfoRequest;
import com.example.superligasparta.model.tournamentTeam.TournamentTeamInfoDto;
import com.example.superligasparta.model.tournamentTeam.UpdateTournamentTeamRequest;
import com.example.superligasparta.service.TournamentTeamInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournament-team-info")
@RequiredArgsConstructor
@Tag(name = "Tournament Team Info", description = "Управление связью между турнирами и командами")
public class TournamentTeamInfoController {

  private final TournamentTeamInfoService tournamentTeamInfoService;

  @PostMapping
  @Operation(summary = "Создать связь турнир-команда")
  public ResponseEntity<Long> create(
      @RequestBody @Valid CreateTournamentTeamInfoRequest request
  ) {
    Long id = tournamentTeamInfoService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить запись по ID")
  public ResponseEntity<TournamentTeamInfoDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(tournamentTeamInfoService.getById(id));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить запись")
  public ResponseEntity<Void> update(
      @PathVariable Long id,
      @RequestBody @Valid UpdateTournamentTeamRequest request
  ) {
    tournamentTeamInfoService.update(id, request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить запись")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    tournamentTeamInfoService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/tournament/{tournamentId}")
  @Operation(summary = "Получить всех участников турнира")
  public ResponseEntity<List<TournamentTeamInfoDto>> getByTournamentId(@PathVariable Long tournamentId) {
    return ResponseEntity.ok(tournamentTeamInfoService.getByTournamentId(tournamentId));
  }

}
