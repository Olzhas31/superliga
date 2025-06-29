package com.example.superligasparta.controller.rest;

import com.example.superligasparta.domain.entity.Tournament;
import com.example.superligasparta.model.tournament.CreateTournamentRequest;
import com.example.superligasparta.model.tournament.TournamentDto;
import com.example.superligasparta.model.tournament.TournamentWithTeamsDto;
import com.example.superligasparta.model.tournament.UpdateTournamentRequest;
import com.example.superligasparta.service.TournamentService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournaments", description = "Управление турнирами")
public class TournamentController {

  private final TournamentService tournamentService;

  @PostMapping
  @Operation(summary = "Создать турнир")
  public ResponseEntity<TournamentDto> create(@RequestBody @Valid CreateTournamentRequest request) {
    return ResponseEntity.ok(tournamentService.createTournament(request));
  }

  @GetMapping
  @Operation(summary = "Список всех турниров")
  public List<Tournament> getAll() {
    return tournamentService.getAllTournaments();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить турнир по ID")
  public ResponseEntity<TournamentDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(tournamentService.getTournamentById(id));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить турнир")
  public ResponseEntity<TournamentDto> update(
      @PathVariable Long id,
      @RequestBody @Valid UpdateTournamentRequest request
  ) {
    return ResponseEntity.ok(tournamentService.updateTournament(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить турнир по ID")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    tournamentService.deleteTournament(id);
  }

  @GetMapping("/{id}/with-teams")
  @Operation(summary = "Получить турнир и список команд, участвующих в нём")
  public TournamentWithTeamsDto getTournamentWithTeams(@PathVariable Long id) {
    return tournamentService.getTournamentWithTeams(id);
  }


}
