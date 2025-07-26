package com.example.superligasparta.controller;

import com.example.superligasparta.domain.entity.Round;
import com.example.superligasparta.model.round.CreateRoundRequest;
import com.example.superligasparta.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/round")
public class RoundController {

  private final RoundService roundService;

  @GetMapping("/new")
  public String showNewRoundForm(@RequestParam("tournamentId") Long tournamentId, Model model) {
    model.addAttribute("round", new CreateRoundRequest());
    model.addAttribute("tournamentId", tournamentId);
    return "round/new";
  }

  @PostMapping("/save")
  public String saveRound(@ModelAttribute CreateRoundRequest createRoundRequest) {
    roundService.create(createRoundRequest);
    return "redirect:/schedule?tournamentId=" +createRoundRequest.getTournamentId();
  }

  @GetMapping("/{id}/edit")
  public String showEditRoundForm(@PathVariable("id") Long id, Model model) {
    Round round = roundService.getById(id);
    model.addAttribute("round", round);
    return "round/edit";
  }

  @PostMapping("/edit")
  public String updateRound(@ModelAttribute Round round) {
    roundService.update(round);
    return "redirect:/schedule?tournamentId=" + round.getTournamentId();
  }
}
