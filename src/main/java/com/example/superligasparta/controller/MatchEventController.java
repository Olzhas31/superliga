package com.example.superligasparta.controller;

import com.example.superligasparta.model.match.MatchEventDto;
import com.example.superligasparta.service.MatchCardService;
import com.example.superligasparta.service.MatchEventService;
import com.example.superligasparta.service.MatchGoalService;
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
  private final MatchCardService matchCardService;
  private final MatchGoalService matchGoalService;

  @GetMapping("/new")
  public String showCreateMatchEventPage(Model model) {
    model.addAttribute("matchEvent", new MatchEventDto());
    return "match/match-event";
  }

  @PostMapping("/{id}/delete")
  public String deleteMatchEvent(@PathVariable Long id,
      @RequestParam Long matchId,
      @RequestParam String eventType) {
   if (eventType.equals("RED") || eventType.equals("YELLOW")) {
     matchCardService.deleteById(id);
   } else if (eventType.equals("GOAL") || eventType.equals("PENALTY") || eventType.equals("OWN_GOAL")) {
     matchGoalService.deleteById(id);
   }
    return "redirect:/match/" + matchId;
  }

}
