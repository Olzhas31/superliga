package com.example.superligasparta.model.matchGoal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchScore {

  private Integer homeGoals;
  private Integer awayGoals;
}
