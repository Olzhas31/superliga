package com.example.superligasparta.model.playerContract;

import com.example.superligasparta.model.enums.PlayerPosition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerContractRequest {

  @NotNull(message = "ID игрока обязателен")
  private Long playerId;

  @NotNull(message = "ID команды в турнире обязателен")
  private Long tournamentTeamInfoId;

  @NotNull(message = "Дата начала обязательна")
  private LocalDate startDate;

  private LocalDate endDate;

  @NotNull(message = "Номер игрока обязательна")
  @Min(value = 1, message = "Номер должен быть больше 0")
  private Integer shirtNumber;

  @NotNull(message = "Позиция игрока обязательна")
  private PlayerPosition playerPosition;

}
