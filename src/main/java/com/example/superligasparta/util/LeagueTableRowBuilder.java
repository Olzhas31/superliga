package com.example.superligasparta.util;

import com.example.superligasparta.model.stats.LeagueTableRow;

public class LeagueTableRowBuilder {

  private final Long teamId;
  private final String teamName;
  private int played = 0, wins = 0, draws = 0, losses = 0, goalsFor = 0, goalsAgainst = 0;

  public LeagueTableRowBuilder(Long teamId, String teamName) {
    this.teamId = teamId;
    this.teamName = teamName;
  }

  public void addMatch(int goalsScored, int goalsConceded) {
    played++;
    goalsFor += goalsScored;
    goalsAgainst += goalsConceded;
    if (goalsScored > goalsConceded) wins++;
    else if (goalsScored == goalsConceded) draws++;
    else losses++;
  }

  public LeagueTableRow build() {
    return new LeagueTableRow(
        teamId,
        teamName,
        played,
        wins,
        draws,
        losses,
        goalsFor,
        goalsAgainst,
        goalsFor - goalsAgainst,
        wins * 3 + draws
    );
  }
}
