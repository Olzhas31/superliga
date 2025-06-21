package com.example.superligasparta.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTeamToTournamentRequest {
  @NotNull
  private Long teamId;

  @NotBlank
  private String displayName;
}
