package com.example.superligasparta.validation;

import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.PlayerContractRepository;
import com.example.superligasparta.domain.repository.PlayerRepository;
import com.example.superligasparta.domain.repository.TeamRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityValidator {

  private final TournamentRepository tournamentRepository;
  private final TeamRepository teamRepository;
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;
  private final PlayerContractRepository playerContractRepository;
  private final PlayerRepository playerRepository;

  public void validateTournamentExists(Long tournamentId) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new EntityNotFoundException("Турнир с id " + tournamentId + " не найден");
    }
  }

  public void validateTournamentNameIsUnique(String name) {
    if (tournamentRepository.existsByName(name)) {
      throw new IllegalArgumentException("Турнир с названием " + name + " уже существует");
    }
  }

  public void validateTeamExists(Long teamId) {
    if (!teamRepository.existsById(teamId)) {
      throw new EntityNotFoundException("Команда с id " + teamId + " не найдена");
    }
  }

  public void validateTeamNotAlreadyInTournament(Long tournamentId, Long teamId) {
    if (tournamentTeamInfoRepository.existsByTournamentIdAndTeamId(tournamentId, teamId)) {
      throw new IllegalArgumentException("Команда уже добавлена в турнир");
    }
  }

  public void validateDisplayNameIsUniqueInTournament(Long tournamentId, String displayName) {
    if (tournamentTeamInfoRepository.existsByTournamentIdAndDisplayName(tournamentId, displayName)) {
      throw new IllegalArgumentException("Команда с таким названием уже участвует в турнире");
    }
  }

  // TODO изменить название так как используется и в других целях
  public void validateContractBelongsToTournamentTeamInfo(Long contractId, Long tournamentTeamInfoId) {
    PlayerContract contract = playerContractRepository.findById(contractId)
        .orElseThrow(() -> new EntityNotFoundException("Контракт игрока не найден"));

    if (!contract.getTournamentTeamInfoId().equals(tournamentTeamInfoId)) {
      throw new IllegalArgumentException("Контракт не принадлежит указанной команде в турнире");
    }

    if (!contract.isActive()) {
      throw new IllegalStateException("Нельзя использовать неактивный контракт");
    }
  }

  public void validatePlayerExists(Long playerId) {
    if (!playerRepository.existsById(playerId)) {
      throw new EntityNotFoundException("Игрок с id " + playerId + " не найден");
    }
  }

  public void validateTournamentTeamInfoExists(Long tournamentTeamInfoId) {
    if (!tournamentTeamInfoRepository.existsById(tournamentTeamInfoId)) {
      throw new EntityNotFoundException("Команда для турнира с id = " + tournamentTeamInfoId + " не найден");
    }
  }

  public void validatePlayerNotInAnotherTeamInTournament(
      Long playerId,
      Long tournamentTeamInfoId,
      LocalDate startDate,
      LocalDate endDate) {
    TournamentTeamInfo teamInfo = tournamentTeamInfoRepository.findById(tournamentTeamInfoId)
        .orElseThrow(() -> new EntityNotFoundException("Участник турнира с id = " + tournamentTeamInfoId + " не найден"));

    boolean exists = playerContractRepository.existsInAnotherTeam(
        playerId,
        teamInfo.getTournamentId(),
        tournamentTeamInfoId,
        startDate,
        endDate
    );

    if (exists) {
      throw new IllegalStateException("Игрок уже зарегистрирован за другую команду в этом турнире");
    }
  }

  public void validateShirtNumberIsAvailable(
      Long tournamentTeamInfoId,
      Integer shirtNumber,
      LocalDate startDate,
      LocalDate endDate
  ) {
    boolean taken = playerContractRepository.isShirtNumberTaken(
        tournamentTeamInfoId,
        shirtNumber,
        startDate,
        endDate
    );

    if (taken) {
      throw new IllegalStateException("Номер " + shirtNumber + " уже занят в команде на этот период");
    }
  }

}