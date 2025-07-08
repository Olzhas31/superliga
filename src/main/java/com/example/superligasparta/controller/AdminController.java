package com.example.superligasparta.controller;

import com.example.superligasparta.domain.entity.Tournament;
import com.example.superligasparta.model.tournament.CreateTournamentRequest;
import com.example.superligasparta.model.tournament.TournamentDto;
import com.example.superligasparta.model.tournament.UpdateTournamentRequest;
import com.example.superligasparta.service.TournamentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  private final TournamentService tournamentService;

  @GetMapping
  public String homePage(Model model) {
    return "admin";
  }

  @GetMapping("/tournaments")
  public String tournamentsPage(Model model) {
    List<Tournament> tournaments = tournamentService.getAllTournaments();
    model.addAttribute("tournaments", tournaments);
    return "admin/admin-tournaments";
  }

  @GetMapping("/tournaments/new")
  public String createTournamentPage(Model model) {
    model.addAttribute("tournament", new TournamentDto());
    return "admin/create-tournament";
  }

  @PostMapping("/tournaments")
  public String createTournament(@ModelAttribute CreateTournamentRequest request) {
    tournamentService.createTournament(request);
    return "redirect:/admin/tournaments";
  }

  @GetMapping("/tournaments/{id}/edit")
  public String editTournamentPage(@PathVariable Long id, Model model) {
    TournamentDto tournament = tournamentService.getTournamentById(id);
    model.addAttribute("tournament", tournament);
    return "admin/edit-tournament";
  }

  @PatchMapping("/tournaments/{id}")
  public String updateTournament(@PathVariable Long id,
      @ModelAttribute UpdateTournamentRequest tournamentDto) {
    tournamentService.updateTournament(id, tournamentDto);
    return "redirect:/admin/tournaments";
  }


}
