package com.example.superligasparta.controller;

import com.example.superligasparta.domain.entity.Player;
import com.example.superligasparta.domain.entity.PlayerContract;
import com.example.superligasparta.model.enums.MatchEventType;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.match.MatchEventDto;
import com.example.superligasparta.service.MatchEventService;
import com.example.superligasparta.service.MatchService;
import com.example.superligasparta.service.PlayerContractService;
import com.example.superligasparta.service.PlayerService;
import java.util.List;
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
@RequestMapping("/match-event")
public class MatchEventController {

  private final MatchEventService matchEventService;
  private final MatchService matchService;
  private final PlayerService playerService;
  private final PlayerContractService playerContractService;

  @GetMapping("/new")
  public String showCreateMatchEventPage(@RequestParam("matchId") Long matchId, Model model) {
    MatchDto matchDto = matchService.getMatchById(matchId);
    List<PlayerContract> homePlayersContracts = playerContractService.getPlayerContractsByTeamInfoIdAndDate(
        matchDto.getHomeParticipantId(), matchDto.getMatchDateTime().toLocalDate());
    List<Player> homePlayers = playerService.getAllByIds(homePlayersContracts.stream().map(
        PlayerContract::getPlayerId).toList());

    List<PlayerContract> awayPlayersContracts = playerContractService.getPlayerContractsByTeamInfoIdAndDate(
        matchDto.getAwayParticipantId(), matchDto.getMatchDateTime().toLocalDate());
    List<Player> awayPlayers = playerService.getAllByIds(awayPlayersContracts.stream().map(
        PlayerContract::getPlayerId).toList());

    model.addAttribute("match", matchDto);
    model.addAttribute("matchEvent", new MatchEventDto());
    model.addAttribute("eventTypes", MatchEventType.values());
    model.addAttribute("homePlayers", homePlayers);
    model.addAttribute("awayPlayers", awayPlayers);
    return "match/match-event";
  }

  @PostMapping("/save")
  public String saveMatchEvent(@ModelAttribute MatchEventDto matchEventDto) {
    matchEventService.save(matchEventDto);
    return "redirect:/match/" + matchEventDto.getMatchId();
  }

  @PostMapping("/{id}/delete")
  public String deleteMatchEvent(@PathVariable Long id,
      @RequestParam Long matchId,
      @RequestParam MatchEventType eventType) {
    matchEventService.deleteByIdAndEventType(id, eventType);
    return "redirect:/match/" + matchId;
  }

}
