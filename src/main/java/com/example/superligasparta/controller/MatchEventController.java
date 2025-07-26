package com.example.superligasparta.controller;

import com.example.superligasparta.model.enums.MatchEventType;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.match.MatchEventDto;
import com.example.superligasparta.service.MatchEventService;
import com.example.superligasparta.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match-event")
public class MatchEventController {

  private final MatchEventService matchEventService;
  private final MatchService matchService;

  @GetMapping("/new")
  public String showCreateMatchEventPage(@RequestParam("matchId") Long matchId, Model model) {
    MatchDto matchDto = matchService.getMatchById(matchId);
    model.addAttribute("match", matchDto);
    model.addAttribute("matchEvent", new MatchEventDto());
    return "match/match-event";
  }

  @PostMapping("/{id}/delete")
  public String deleteMatchEvent(@PathVariable Long id,
      @RequestParam Long matchId,
      @RequestParam MatchEventType eventType) {
    matchEventService.deleteByIdAndEventType(id, eventType);
    return "redirect:/match/" + matchId;
  }

}
