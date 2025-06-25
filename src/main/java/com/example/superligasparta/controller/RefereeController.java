package com.example.superligasparta.controller;

import com.example.superligasparta.model.referee.RefereeRequest;
import com.example.superligasparta.model.referee.RefereeResponse;
import com.example.superligasparta.service.RefereeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/referees")
@RequiredArgsConstructor
@Tag(name = "Referees", description = "CRUD для судей")
public class RefereeController {

  private final RefereeService refereeService;

  @PostMapping
  @Operation(summary = "Создать судью")
  public ResponseEntity<RefereeResponse> create(@RequestBody @Valid RefereeRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(refereeService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить судью")
  public ResponseEntity<RefereeResponse> update(
      @PathVariable Long id,
      @RequestBody @Valid RefereeRequest request
  ) {
    return ResponseEntity.ok(refereeService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить судью")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    refereeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить судью по ID")
  public ResponseEntity<RefereeResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(refereeService.getById(id));
  }

  @GetMapping
  @Operation(summary = "Получить всех судей")
  public ResponseEntity<List<RefereeResponse>> getAll() {
    return ResponseEntity.ok(refereeService.getAll());
  }
}
