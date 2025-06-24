package com.example.superligasparta.controller;

import com.example.superligasparta.domain.entity.Player;
import com.example.superligasparta.model.player.CreatePlayerRequest;
import com.example.superligasparta.model.player.UpdatePlayerRequest;
import com.example.superligasparta.service.PlayerService;
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
@RequestMapping("/players")
@RequiredArgsConstructor
@Tag(name = "Players", description = "Управление футболистами")
public class PlayerController {

  private final PlayerService playerService;

  @PostMapping
  @Operation(summary = "Создать нового игрока")
  public ResponseEntity<Player> create(@RequestBody @Valid CreatePlayerRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить информацию об игроке")
  public ResponseEntity<Player> update(@PathVariable Long id, @RequestBody @Valid UpdatePlayerRequest request) {
    return ResponseEntity.ok(playerService.update(id, request));
  }

  @GetMapping
  @Operation(summary = "Получить всех игроков")
  public ResponseEntity<List<Player>> getAll() {
    return ResponseEntity.ok(playerService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить игрока по ID")
  public ResponseEntity<Player> getById(@PathVariable Long id) {
    return ResponseEntity.ok(playerService.getById(id));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить игрока по ID")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    playerService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

