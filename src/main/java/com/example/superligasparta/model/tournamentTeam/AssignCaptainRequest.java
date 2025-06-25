package com.example.superligasparta.model.tournamentTeam;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignCaptainRequest {
  @NotNull
  private Long captainContractId;
}

