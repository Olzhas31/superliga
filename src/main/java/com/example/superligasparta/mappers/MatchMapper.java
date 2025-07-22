package com.example.superligasparta.mappers;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.model.match.MatchDto;

public class MatchMapper {

  public static MatchDto toDto(Match match, String homeTeamName, String awayTeamName) {
    return MatchDto.builder()
        .id(match.getId())
        .homeParticipantId(match.getHomeParticipantId())
        .awayParticipantId(match.getAwayParticipantId())
        .homeTeamName(homeTeamName)
        .awayTeamName(awayTeamName)
        .homeGoals(match.getHomeGoals())
        .awayGoals(match.getAwayGoals())
        .played(match.getPlayed())
        .matchDateTime(match.getMatchDateTime())
        .tournamentId(match.getTournamentId())
        .roundId(match.getRoundId())
        .build();
  }
}