package com.example.superligasparta.mappers;

import com.example.superligasparta.domain.entity.Match;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.matchGoal.MatchScore;

public class MatchMapper {

  public static MatchDto toDto(Match match, TournamentTeamInfo homeTeam, TournamentTeamInfo awayTeam,
      MatchScore matchScore) {
    MatchDto matchDto = MatchDto.builder()
        .id(match.getId())
        .homeParticipantId(match.getHomeParticipantId())
        .awayParticipantId(match.getAwayParticipantId())
        .homeTeamName(homeTeam.getDisplayName())
        .awayTeamName(awayTeam.getDisplayName())
        .homeTeamId(homeTeam.getTeamId())
        .awayTeamId(awayTeam.getTeamId())
        .played(match.getPlayed())
        .matchDateTime(match.getMatchDateTime())
        .tournamentId(match.getTournamentId())
        .roundId(match.getRoundId())
        .build();
    if (matchScore != null) {
      matchDto.setHomeGoals(matchScore.getHomeGoals());
      matchDto.setAwayGoals(matchScore.getAwayGoals());
    }
    return matchDto;
  }
}