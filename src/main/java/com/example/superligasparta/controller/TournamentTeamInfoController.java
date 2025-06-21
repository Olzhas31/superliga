package com.example.superligasparta.controller;

import com.example.superligasparta.model.AddTeamToTournamentRequest;
import com.example.superligasparta.model.tournament.UpdateTournamentTeamRequest;
import com.example.superligasparta.service.TournamentTeamInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournament-teams")
@RequiredArgsConstructor
@Tag(name = "Tournament Teams", description = "Управление командами в турнирах")
public class TournamentTeamInfoController {

  private final TournamentTeamInfoService tournamentTeamInfoService;

  @PostMapping("/{tournamentId}/teams")
  @Operation(summary = "Добавить команду в турнир")
  public void addTeamToTournament(
      @PathVariable Long tournamentId,
      @RequestBody @Valid AddTeamToTournamentRequest request
  ) {
    tournamentTeamInfoService.addTeamToTournament(tournamentId, request);
  }

  @PatchMapping("/{tournamentId}/teams/{teamId}")
  @Operation(summary = "Изменить данные команды в турнире")
  public ResponseEntity<Void> updateTournamentTeam(
      @PathVariable Long tournamentId,
      @PathVariable Long teamId,
      @RequestBody @Valid UpdateTournamentTeamRequest request) {
    tournamentTeamInfoService.updateTournamentTeamInfo(tournamentId, teamId, request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{tournamentId}/teams/{teamId}")
  @Operation(summary = "Удалить команду из турнира")
  public ResponseEntity<Void> delete(
      @PathVariable Long tournamentId,
      @PathVariable Long teamId) {
    tournamentTeamInfoService.removeTeamFromTournament(tournamentId, teamId);
    return ResponseEntity.noContent().build();
  }
}
