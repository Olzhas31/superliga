package com.example.superligasparta.model.tournamentTeam;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTournamentTeamRequest {

  @NotNull
  private Long tournamentId;

  @NotNull
  private Long teamId;

  @NotBlank(message = "Отображаемое имя команды не может быть пустым")
  private String displayName;

  // TODO нужно картинки загружать
  @Schema(description = "URL логотипа команды в турнире")
  private String logoUrl;

}
