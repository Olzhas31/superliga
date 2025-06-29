package com.example.superligasparta.controller.rest;

import com.example.superligasparta.model.referee.RefereeResponse;
import com.example.superligasparta.service.TournamentRefereeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournaments/{tournamentId}/referees")
@RequiredArgsConstructor
@Tag(name = "Tournament Referees", description = "Назначение судей на турниры")
public class TournamentRefereeController {

  private final TournamentRefereeService tournamentRefereeService;

  @PostMapping("/{refereeId}")
  @Operation(summary = "Назначить судью на турнир")
  public ResponseEntity<Void> assignReferee(
      @PathVariable Long tournamentId,
      @PathVariable Long refereeId
  ) {
    tournamentRefereeService.assignReferee(tournamentId, refereeId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{refereeId}")
  @Operation(summary = "Удалить судью из турнира")
  public ResponseEntity<Void> removeReferee(
      @PathVariable Long tournamentId,
      @PathVariable Long refereeId
  ) {
    tournamentRefereeService.removeReferee(tournamentId, refereeId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Получить всех судей турнира")
  public ResponseEntity<List<RefereeResponse>> getReferees(@PathVariable Long tournamentId) {
    return ResponseEntity.ok(tournamentRefereeService.getRefereesByTournament(tournamentId));
  }
}

