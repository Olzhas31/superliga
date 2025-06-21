package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Round;
import com.example.superligasparta.model.round.CreateRoundRequest;
import java.util.List;

public interface RoundService {

  Round create(CreateRoundRequest request);

  List<Round> getByTournament(Long tournamentId);
}

