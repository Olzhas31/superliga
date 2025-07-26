package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.model.playerContract.CreatePlayerContractRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlayerContractService {

  PlayerContract createContract(CreatePlayerContractRequest request);

  Optional<PlayerContract> findActiveContractForMatch(Long playerId, Long homeTeamInfoId, Long awayTeamInfoId, LocalDate matchDate);

  List<PlayerContract> getPlayerContractsByTeamInfoIdAndDate(Long homeParticipantId,
      LocalDate matchDate);

//  List<PlayerContract> getContractsByPlayerAndTournament(Long playerId, Long tournamentId);

  // TODO terminateContract(contractId, endDate)
}

