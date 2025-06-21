package com.example.superligasparta.model.tournament;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с данными турнира и его участниками")
public class TournamentWithTeamsDto {
  private Long id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<Long> teamIds;
}

