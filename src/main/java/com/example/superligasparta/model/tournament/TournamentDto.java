package com.example.superligasparta.model.tournament;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TournamentDto {
  private Long id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private Boolean archived;
}
