package com.example.superligasparta.controller;

import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.MatchDto;
import com.example.superligasparta.model.tournamentTeam.TournamentTeamInfoDto;
import com.example.superligasparta.service.MatchService;
import com.example.superligasparta.service.TournamentTeamInfoService;
import jakarta.validation.Valid;
import java.time.format.DateTimeFormatter;
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
@RequestMapping("/match")
public class MatchController {

  private final MatchService matchService;
  private final TournamentTeamInfoService tournamentTeamInfoService;

  @GetMapping("/{id}")
  public String showMatch(@PathVariable Long id, Model model) {
    MatchDto matchDto = matchService.getMatchById(id);
    model.addAttribute("match", matchDto);
    return "match/match";
  }

  @GetMapping("/new")
  public String showCreateMatch(@RequestParam Long tournamentId, @RequestParam Long roundId, Model model) {
    List<TournamentTeamInfoDto> tournamentTeamInfos = tournamentTeamInfoService.getByTournamentId(tournamentId);

    CreateMatchRequest request = new CreateMatchRequest();
    request.setTournamentId(tournamentId);
    request.setRoundId(roundId);

    model.addAttribute("tournamentTeamInfos", tournamentTeamInfos);
    model.addAttribute("match", request);
    return "match/new";
  }

  @PostMapping("/new")
  public String createMatch(@Valid @ModelAttribute("match") CreateMatchRequest matchRequest) {
    matchService.createMatch(matchRequest);
    return "redirect:/schedule?tournamentId" + matchRequest.getTournamentId();
  }

  @GetMapping("/{id}/edit")
  public String showEditMatch(@PathVariable Long id, Model model) {
    MatchDto matchDto = matchService.getMatchById(id);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String formattedDateTime = matchDto.getMatchDateTime().format(formatter);
    model.addAttribute("matchDateTimeFormatted", formattedDateTime);

    model.addAttribute("match", matchDto);
    return "match/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateMatch(@PathVariable Long id, @ModelAttribute MatchDto matchDto) {
    matchService.updateMatch(matchDto);
    return "redirect:/match/" + id;
  }

}
