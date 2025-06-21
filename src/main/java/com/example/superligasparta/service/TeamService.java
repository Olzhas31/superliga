package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Team;
import com.example.superligasparta.model.team.CreateTeamRequest;
import com.example.superligasparta.model.team.UpdateTeamRequest;
import java.util.List;

public interface TeamService {

  Team createTeam(CreateTeamRequest team);

  List<Team> getAllTeams();

  Team getTeamById(Long id);

  Team updateTeam(Long id, UpdateTeamRequest team);

  void deleteTeam(Long id);

}
