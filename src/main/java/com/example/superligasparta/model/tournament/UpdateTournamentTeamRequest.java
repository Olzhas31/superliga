package com.example.superligasparta.model.tournament;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTournamentTeamRequest {

  @NotBlank(message = "Отображаемое имя команды не может быть пустым")
  private String displayName;

}
