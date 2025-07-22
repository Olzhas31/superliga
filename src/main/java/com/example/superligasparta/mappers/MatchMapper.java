package com.example.superligasparta.mappers;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.model.match.MatchDto;

public class MatchMapper {

  public static MatchDto toDto(Match match, TournamentTeamInfo homeTeam, TournamentTeamInfo awayTeam) {
    return MatchDto.builder()
        .id(match.getId())
        .homeParticipantId(match.getHomeParticipantId())
        .awayParticipantId(match.getAwayParticipantId())
        .homeTeamName(homeTeam.getDisplayName())
        .awayTeamName(awayTeam.getDisplayName())
        .homeTeamId(homeTeam.getTeamId())
        .awayTeamId(awayTeam.getTeamId())
        .homeGoals(match.getHomeGoals())
        .awayGoals(match.getAwayGoals())
        .played(match.getPlayed())
        .matchDateTime(match.getMatchDateTime())
        .tournamentId(match.getTournamentId())
        .roundId(match.getRoundId())
        .build();
  }
}