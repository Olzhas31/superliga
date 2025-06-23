package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.domain.repository.PlayerContractRepository;
import com.example.superligasparta.model.playerContract.CreatePlayerContractRequest;
import com.example.superligasparta.service.PlayerContractService;
import com.example.superligasparta.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerContractServiceImpl implements PlayerContractService {

  private final PlayerContractRepository repository;
  private final EntityValidator entityValidator;

  @Override
  public PlayerContract createContract(CreatePlayerContractRequest request) {
    entityValidator.validatePlayerExists(request.getPlayerId());
    entityValidator.validateTournamentTeamInfoExists(request.getTournamentTeamInfoId());
    entityValidator.validatePlayerNotInAnotherTeamInTournament(
        request.getPlayerId(),
        request.getTournamentTeamInfoId(),
        request.getStartDate(),
        request.getEndDate()
    );
    validateNoContractConflict(request);
    entityValidator.validateShirtNumberIsAvailable(
        request.getTournamentTeamInfoId(),
        request.getShirtNumber(),
        request.getStartDate(),
        request.getEndDate()
    );

    PlayerContract contract = PlayerContract.builder()
        .playerId(request.getPlayerId())
        .tournamentTeamInfoId(request.getTournamentTeamInfoId())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .shirtNumber(request.getShirtNumber())
        .position(request.getPlayerPosition())
        .build();
    return repository.save(contract);
  }

//  @Override
//  public List<PlayerContract> getContractsByPlayerAndTournament(Long playerId, Long tournamentId) {
    // TODO
//    return repository.findByPlayerIdAndTournamentId(playerId, tournamentId);
//    return null;
//  }

  private void validateNoContractConflict(CreatePlayerContractRequest request) {
    boolean conflict = repository.existsConflictingContract(
        request.getPlayerId(),
        request.getTournamentTeamInfoId(),
        request.getStartDate(),
        request.getEndDate()
    );

    if (conflict) {
      throw new IllegalStateException("У игрока уже есть контракт в этом турнире на указанный период");
    }
  }

}

