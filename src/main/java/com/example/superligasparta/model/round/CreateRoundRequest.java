package com.example.superligasparta.model.round;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание тура")
public class CreateRoundRequest {

  @NotNull(message = "ID турнира не должен быть null")
  @Schema(description = "ID турнира, к которому относится тур", example = "1")
  private Long tournamentId;

  @NotBlank(message = "Название тура не должно быть пустым")
  @Schema(description = "Название тура", example = "1 тур")
  private String name;

  @Schema(description = "Порядок для сортировки")
  private Integer order;
}

