package com.example.superligasparta.controller;

import com.example.superligasparta.domain.entity.Team;
import com.example.superligasparta.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

  private final TeamService teamService;

  @GetMapping("/{id}")
  public String showTeamPage(@PathVariable Long id, @RequestParam(required = false) Long tournamentId,  Model model) {
    // TODO if tournamentId = null показать последний сезон
    Team team = teamService.getTeamById(id);
    model.addAttribute("team", team);
    return "team/team";
  }
}
