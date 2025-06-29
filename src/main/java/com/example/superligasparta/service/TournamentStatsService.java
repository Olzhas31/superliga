package com.example.superligasparta.service;

import com.example.superligasparta.model.stats.LeagueTableRow;
import com.example.superligasparta.model.stats.TeamFairPlayStatsDto;
import com.example.superligasparta.model.stats.TopScorerDto;
import java.util.List;

public interface TournamentStatsService {

  List<LeagueTableRow> getLeagueTable(Long tournamentId);

  List<TopScorerDto> getScorers(Long tournamentId);

  List<TeamFairPlayStatsDto> getFairPlayStats(Long tournamentId);
}
