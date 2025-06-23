package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.model.playerContract.CreatePlayerContractRequest;
import java.util.List;

public interface PlayerContractService {

  PlayerContract createContract(CreatePlayerContractRequest request);

//  List<PlayerContract> getContractsByPlayerAndTournament(Long playerId, Long tournamentId);

  // TODO terminateContract(contractId, endDate)
}

