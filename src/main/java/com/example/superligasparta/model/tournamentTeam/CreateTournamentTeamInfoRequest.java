package com.example.superligasparta.model.tournamentTeam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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