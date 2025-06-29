package com.example.superligasparta.controller.rest;

import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.model.playerContract.CreatePlayerContractRequest;
import com.example.superligasparta.service.PlayerContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@Tag(name = "Player Contracts", description = "Контракты игроков с командами на турниры")
public class PlayerContractController {

  private final PlayerContractService service;

  @PostMapping
  @Operation(summary = "Заключить контракт игрока с командой")
  public ResponseEntity<PlayerContract> create(@RequestBody @Valid CreatePlayerContractRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.createContract(request));
  }

//  @GetMapping("/by-player/{playerId}/tournament/{tournamentId}")
//  @Operation(summary = "Получить контракты игрока в турнире")
//  public List<PlayerContract> getByPlayerAndTournament(@PathVariable Long playerId,
//      @PathVariable Long tournamentId) {
//    return service.getContractsByPlayerAndTournament(playerId, tournamentId);
//  }
}
