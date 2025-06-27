package com.example.superligasparta.model.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopScorerDto {

  private Long playerId;
  private String playerName;
  private int score;        // +1 за гол, -1 за автогол
  private int goals;
  private int ownGoals;
  private String teamName;

}
