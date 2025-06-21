package com.example.superligasparta.model.tournament;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTournamentRequest {

  @NotBlank(message = "Название турнира не может быть пустым")
  private String name;

  @NotNull(message = "Дата начала турнира обязательна")
  private LocalDate startDate;

  @NotNull(message = "Дата окончания турнира обязательна")
  private LocalDate endDate;
}
