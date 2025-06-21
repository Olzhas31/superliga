package com.example.superligasparta.service;

import com.example.superligasparta.model.AddTeamToTournamentRequest;
import com.example.superligasparta.model.tournament.UpdateTournamentTeamRequest;

public interface TournamentTeamInfoService {

  void addTeamToTournament(Long tournamentId, AddTeamToTournamentRequest request);

  void updateTournamentTeamInfo(Long tournamentId, Long teamId, UpdateTournamentTeamRequest request);

  void removeTeamFromTournament(Long tournamentId, Long teamId);

}
