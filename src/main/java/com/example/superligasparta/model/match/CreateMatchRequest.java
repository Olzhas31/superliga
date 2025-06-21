package com.example.superligasparta.model.match;

import com.example.superligasparta.configuration.validation.ValidMatchRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание матча")
@ValidMatchRequest
public class CreateMatchRequest {

  @NotNull(message = "ID домашней команды не может быть пустым")
  @Schema(description = "ID домашней команды")
  private Long homeTeamId;

  @NotNull(message = "ID гостевой команды не может быть пустым")
  @Schema(description = "ID гостевой команды")
  private Long awayTeamId;

  @Min(value = 0, message = "Голы домашней команды не могут быть меньше 0")
  @Schema(description = "Голы домашней команды", nullable = true)
  private Integer homeGoals;

  @Min(value = 0, message = "Голы гостевой команды не могут быть меньше 0")
  @Schema(description = "Голы гостевой команды", nullable = true)
  private Integer awayGoals;

  @Schema(description = "Дата и время матча", example = "2025-06-20T18:00:00", nullable = true)
  private LocalDateTime matchDateTime;

  @NotNull(message = "Поле 'played' обязательно для заполнения")
  @Schema(description = "Признак, был ли матч сыгран")
  private Boolean played;

  @NotNull(message = "ID турнира не может быть пустым")
  @Schema(description = "ID турнира")
  private Long tournamentId;

  @NotNull(message = "ID тура не может быть пустым")
  @Schema(description = "ID тура")
  private Long roundId;
}

