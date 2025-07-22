package com.example.superligasparta.model.match;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchDto {

  private Long id;
  private Long homeParticipantId;
  private Long awayParticipantId;
  private String homeTeamName;
  private String awayTeamName;
  private Long homeTeamId;
  private Long awayTeamId;
  private Integer homeGoals;
  private Integer awayGoals;
  private Boolean played;
  private LocalDateTime matchDateTime;
  private Long tournamentId;
  private Long roundId;
}
