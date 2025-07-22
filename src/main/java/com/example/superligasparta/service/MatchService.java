package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.match.UpdateMatchRequest;
import java.util.List;

public interface MatchService {

  List<Match> getAllMatches();

  List<Match> getMatchesByTournament(Long tournamentId);

  MatchDto getMatchById(Long id);

  Match createMatch(CreateMatchRequest match);

  Match updateMatch(Long id, UpdateMatchRequest match);

  void deleteMatch(Long id);
}
