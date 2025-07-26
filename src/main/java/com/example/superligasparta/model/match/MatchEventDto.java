package com.example.superligasparta.model.match;

import com.example.superligasparta.model.enums.MatchEventType;
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
  private Long matchId;
  private Integer minute;
  private MatchEventType type;
  private String playerName;
  private Long playerId;
  private boolean homeTeam; // true - home, false - away
}
