package com.example.superligasparta.model.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchEventDto {
  private Long id;
  private Integer minute;
  private String type; // TODO сделать enum
  private String playerName;
  private Long playerId;
  private boolean homeTeam; // true - home, false - away
}
