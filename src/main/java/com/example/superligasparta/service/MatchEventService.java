package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.model.enums.MatchEventType;
import com.example.superligasparta.model.match.MatchEventDto;
import com.example.superligasparta.model.matchGoal.MatchScore;

public interface MatchEventService {

  MatchScore calculateScore(Match match);

  void deleteByIdAndEventType(Long id, MatchEventType eventType);

  void save(MatchEventDto matchEventDto);
}
