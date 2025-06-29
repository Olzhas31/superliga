package com.example.superligasparta.controller;

import com.example.superligasparta.model.stats.LeagueTableRow;
import com.example.superligasparta.service.TournamentStatsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final TournamentStatsService tournamentStatsService;

  @GetMapping("/")
  public String homePage(Model model) {
    // TODO по умолчанию показывать активный турнир
    List<LeagueTableRow> teams = tournamentStatsService.getLeagueTable(6L);
    model.addAttribute("teams", teams);
    return "index";
  }

  @GetMapping("/calendar")
  public String showCalendar(Model model) {
    return "calendar";
  }

  @GetMapping("/scores")
  public String showTopScores(Model model) {
    return "scores";
  }

  @GetMapping("/giant-killers")
  public String showGiantKillers(Model model) {
    return "giant-killers";
  }

  @GetMapping("/fair-play")
  public String test(Model model) {
    return "fair-play";
  }
}
