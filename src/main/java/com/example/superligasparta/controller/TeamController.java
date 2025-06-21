package com.example.superligasparta.controller;

import com.example.superligasparta.domain.entity.Team;
import com.example.superligasparta.model.team.CreateTeamRequest;
import com.example.superligasparta.model.team.UpdateTeamRequest;
import com.example.superligasparta.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

  private final TeamService teamService;

  @PostMapping
  public ResponseEntity<Team> createTeam(@RequestBody CreateTeamRequest team) {
    return ResponseEntity.ok(teamService.createTeam(team));
  }

  @GetMapping
  public ResponseEntity<List<Team>> getAllTeams() {
    return ResponseEntity.ok(teamService.getAllTeams());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
    return ResponseEntity.ok(teamService.getTeamById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody UpdateTeamRequest team) {
    return ResponseEntity.ok(teamService.updateTeam(id, team));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
    teamService.deleteTeam(id);
    return ResponseEntity.noContent().build();
  }

}