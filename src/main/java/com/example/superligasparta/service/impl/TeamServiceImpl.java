package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Team;
import com.example.superligasparta.domain.repository.TeamRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.model.team.CreateTeamRequest;
import com.example.superligasparta.model.team.UpdateTeamRequest;
import com.example.superligasparta.service.TeamService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

  private final TeamRepository teamRepository;
  private final TournamentRepository tournamentRepository;

  @Override
  public Team createTeam(CreateTeamRequest request) {
    if (teamRepository.existsByName(request.getName())) {
      throw new EntityExistsException("Team with name " + request.getName() + " already exists");
    }
    Team team = new Team();
    team.setName(request.getName());
    return teamRepository.save(team);
  }

  @Override
  public List<Team> getAllTeams() {
    return teamRepository.findAll();
  }

  @Override
  public Team getTeamById(Long id) {
    return Optional.of(teamRepository.findById(id)).get()
        .orElseThrow(() -> new EntityNotFoundException("Team not found with id " + id));
  }

  @Override
  public Team updateTeam(Long id, UpdateTeamRequest updatedTeam) {
    if (teamRepository.existsByName(updatedTeam.getName())) {
      throw new EntityExistsException("Team with name " + updatedTeam.getName() + " already exists");
    }

    return teamRepository.findById(id)
        .map(team -> {
          team.setName(updatedTeam.getName());
          return teamRepository.save(team);
        })
        .orElseThrow(() -> new EntityNotFoundException("Team not found with id " + id));
  }

  @Override
  public void deleteTeam(Long id) {
    if (!teamRepository.existsById(id)) {
      throw new EntityNotFoundException("Team not found with id " + id);
    }
    teamRepository.deleteById(id);
  }


}