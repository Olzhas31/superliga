package com.example.superligasparta.controller;

import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

  private final MatchService matchService;

  @GetMapping("/{id}")
  public String showMatch(@PathVariable Long id, Model model) {
    MatchDto matchDto = matchService.getMatchById(id);
    model.addAttribute("match", matchDto);
    return "match/match";
  }
}
