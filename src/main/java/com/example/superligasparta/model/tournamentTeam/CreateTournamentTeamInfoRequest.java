package com.example.superligasparta.model.tournamentTeam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTournamentTeamInfoRequest {

  @NotNull
  private Long tournamentId;

  @NotNull
  private Long teamId;

  @NotBlank
  private String displayName;

  // TODO нужно картинки загружать
  private String logoUrl;
}