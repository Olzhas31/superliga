package com.example.superligasparta.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamFairPlayStatsDto {
  private Long tournamentTeamInfoId;
  private String displayName;
  private int yellowCards;
  private int redCards;
  private int totalCards;
}

