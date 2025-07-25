package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.model.matchGoal.MatchScore;

public interface MatchGoalService {

  MatchScore calculateScore(Match match);

  void deleteById(Long id);
}
