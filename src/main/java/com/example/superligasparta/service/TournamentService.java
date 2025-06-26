package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Tournament;
import com.example.superligasparta.model.tournament.CreateTournamentRequest;
import com.example.superligasparta.model.tournament.TournamentDto;
import com.example.superligasparta.model.tournament.TournamentWithTeamsDto;
import com.example.superligasparta.model.tournament.UpdateTournamentRequest;
import java.util.List;

public interface TournamentService {

  // TODO return TournaemntDTO
  List<Tournament> getAllTournaments();

  TournamentDto getTournamentById(Long id);

  TournamentDto createTournament(CreateTournamentRequest createTournamentRequest);

  TournamentDto updateTournament(Long id, UpdateTournamentRequest updateTournamentRequest);

  void deleteTournament(Long id);

  TournamentWithTeamsDto getTournamentWithTeams(Long id);

}
