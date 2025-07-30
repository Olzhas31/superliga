package com.example.superligasparta.model.round;

import com.example.superligasparta.model.match.MatchDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundDto {

  private Long id;
  private String name;
  private Long tournamentId;
  private Integer order;
  private List<MatchDto> matches;
}
