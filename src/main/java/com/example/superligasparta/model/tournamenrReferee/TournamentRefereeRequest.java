package com.example.superligasparta.model.tournamenrReferee;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRefereeRequest {

  @NotNull(message = "ID турнира не должен быть null")
  private Long tournamentId;

  @NotNull(message = "ID судьи не должен быть null")
  private Long refereeId;
}

