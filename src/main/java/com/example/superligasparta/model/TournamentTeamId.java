package com.example.superligasparta.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentTeamId implements Serializable {
  private Long tournamentId;
  private Long teamId;
}
