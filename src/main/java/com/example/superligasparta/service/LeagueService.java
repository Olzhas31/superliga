package com.example.superligasparta.service;

import com.example.superligasparta.model.LeagueTableRow;
import java.util.List;

public interface LeagueService {

  List<LeagueTableRow> getLeagueTable(Long tournamentId);

}
