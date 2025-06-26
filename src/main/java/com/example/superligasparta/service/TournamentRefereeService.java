package com.example.superligasparta.service;

import com.example.superligasparta.model.referee.RefereeResponse;
import java.util.List;

public interface TournamentRefereeService {

  void assignReferee(Long tournamentId, Long refereeId);

  void removeReferee(Long tournamentId, Long refereeId);

  List<RefereeResponse> getRefereesByTournament(Long tournamentId);
}

