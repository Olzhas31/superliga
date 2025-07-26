package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Round;
import com.example.superligasparta.model.round.CreateRoundRequest;
import com.example.superligasparta.model.round.RoundDto;
import java.util.List;

public interface RoundService {

  Round create(CreateRoundRequest request);

  Round getById(Long id);

  List<Round> getByTournament(Long tournamentId);

  List<RoundDto> getRoundsWithMatchesByTournamentId(Long tournamentId);

  void update(Round round);

  void deleteById(Long id);
}

