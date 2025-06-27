package com.example.superligasparta.model.stats;

public record LeagueTableRow(
    Long teamId,
    String teamName,
    int played,
    int wins,
    int draws,
    int losses,
    int goalsFor,
    int goalsAgainst,
    int goalDifference,
    int points
) {}
