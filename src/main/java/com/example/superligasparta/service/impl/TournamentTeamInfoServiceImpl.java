package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Team;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.MatchRepository;
import com.example.superligasparta.domain.repository.PlayerContractRepository;
import com.example.superligasparta.domain.repository.TeamRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.model.AddTeamToTournamentRequest;
import com.example.superligasparta.model.tournament.UpdateTournamentTeamRequest;
import com.example.superligasparta.service.TournamentTeamInfoService;
import com.example.superligasparta.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentTeamInfoServiceImpl implements TournamentTeamInfoService {

  private final TournamentRepository tournamentRepository;
  private final TeamRepository teamRepository;
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;
  private final MatchRepository matchRepository;

  @Override
  public void addTeamToTournament(Long tournamentId, AddTeamToTournamentRequest request) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new EntityNotFoundException("Турнир с id " + tournamentId + " не найден");
    }

    if (!teamRepository.existsById(request.getTeamId())) {
      throw new EntityNotFoundException("Команда с id " + request.getTeamId() + " не найдена");
    }

    boolean exists = tournamentTeamInfoRepository
        .existsByTournamentIdAndTeamId(tournamentId, request.getTeamId());
    if (exists) {
      throw new IllegalArgumentException("Команда уже добавлена в турнир");
    }

    // Проверка: есть ли команда с таким отображаемым названием
    boolean nameExists = tournamentTeamInfoRepository
        .existsByTournamentIdAndDisplayName(tournamentId, request.getDisplayName());
    if (nameExists) {
      throw new IllegalArgumentException("Команда с таким названием уже участвует в турнире");
    }
    TournamentTeamInfo info = new TournamentTeamInfo(
        tournamentId,
        request.getTeamId(),
        request.getDisplayName()
    );

    tournamentTeamInfoRepository.save(info);
  }

  @Override
  public void updateTournamentTeamInfo(Long tournamentId, Long teamId, UpdateTournamentTeamRequest request) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new EntityNotFoundException("Турнир с id " + tournamentId + " не найден");
    }

    if (!teamRepository.existsById(teamId)) {
      throw new EntityNotFoundException("Команда с id " + teamId + " не найдена");
    }

    // Проверяем, участвует ли команда в турнире
    TournamentTeamInfo info = tournamentTeamInfoRepository
        .findByTournamentIdAndTeamId(tournamentId, teamId)
        .orElseThrow(() -> new EntityNotFoundException("Команда не найдена в турнире"));

    boolean duplicateNameExists = tournamentTeamInfoRepository
        .existsByTournamentIdAndDisplayNameIgnoreCaseAndTeamIdNot(tournamentId, request.getDisplayName(), teamId);

    if (duplicateNameExists) {
      throw new IllegalArgumentException("Название команды \"" + request.getDisplayName() + "\" уже занято в этом турнире");
    }

    info.setDisplayName(request.getDisplayName());
    tournamentTeamInfoRepository.save(info);
  }


  @Override
  public void removeTeamFromTournament(Long tournamentId, Long teamId) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new EntityNotFoundException("Турнир с id " + tournamentId + " не найден");
    }

    if (!teamRepository.existsById(teamId)) {
      throw new EntityNotFoundException("Команда с id " + teamId + " не найдена");
    }

    TournamentTeamInfo info = tournamentTeamInfoRepository
        .findByTournamentIdAndTeamId(tournamentId, teamId)
        .orElseThrow(() -> new EntityNotFoundException("Команда не найдена в турнире"));

    boolean hasMatches = matchRepository.existsByTournamentIdAndTeamId(tournamentId, teamId);
    if (hasMatches) {
      throw new IllegalStateException("Нельзя удалить команду — в турнире уже существуют матчи с её участием");
    }

    tournamentTeamInfoRepository.delete(info);
  }

  @Override
  public void assignCaptain(Long tournamentTeamInfoId, Long captainContractId) {
    entityValidator.validateTournamentTeamInfoExists(tournamentTeamInfoId);
    entityValidator.validateContractBelongsToTournamentTeamInfo(captainContractId, tournamentTeamInfoId);
    TournamentTeamInfo teamInfo = tournamentTeamInfoRepository.findById(tournamentTeamInfoId).get();

    teamInfo.setCaptainContractId(captainContractId);
    tournamentTeamInfoRepository.save(teamInfo);
  }
}
