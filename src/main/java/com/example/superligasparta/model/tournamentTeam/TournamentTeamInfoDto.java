package com.example.superligasparta.model.tournamentTeam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentTeamInfoDto {
  private Long id;
  private Long tournamentId;
  private Long teamId;
  private String displayName;
  private Long captainContractId;
  private String logoUrl;
}
