package com.example.superligasparta.service;

import com.example.superligasparta.model.tournamentTeam.CreateTournamentTeamInfoRequest;
import com.example.superligasparta.model.tournamentTeam.TournamentTeamInfoDto;
import com.example.superligasparta.model.tournamentTeam.UpdateTournamentTeamRequest;
import java.util.List;

public interface TournamentTeamInfoService {

  Long create(CreateTournamentTeamInfoRequest request);

  TournamentTeamInfoDto getById(Long id);

  void update(Long id, UpdateTournamentTeamRequest request);

  void delete(Long id);

  List<TournamentTeamInfoDto> getByTournamentId(Long tournamentId);
}
