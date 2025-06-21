package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Tournament;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.TeamRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.model.tournament.CreateTournamentRequest;
import com.example.superligasparta.model.tournament.TournamentWithTeamsDto;
import com.example.superligasparta.model.tournament.UpdateTournamentRequest;
import com.example.superligasparta.service.TournamentService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

  private final TournamentRepository repository;
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;
  private final TeamRepository teamRepository;

  @Override
  public List<Tournament> getAllTournaments() {
    return repository.findAll();
  }

  @Override
  public Tournament getTournamentById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Турнир не найден"));
  }

  @Override
  public Tournament createTournament(CreateTournamentRequest request) {
    if (repository.existsByName(request.getName())) {
      throw new IllegalArgumentException("Турнир с названием " + request.getName() +  " уже существует");
    }

    Tournament tournament = new Tournament();
    tournament.setName(request.getName());
    tournament.setStartDate(request.getStartDate());
    tournament.setEndDate(request.getEndDate());
    return repository.save(tournament);
  }

  @Override
  public Tournament updateTournament(Long id, UpdateTournamentRequest request) {
    Tournament existing = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Турнир с id " + id + " не найден"));

    // Проверка на уникальность имени, если изменилось
    if (!existing.getName().equalsIgnoreCase(request.getName()) &&
        repository.existsByName(request.getName())) {
      throw new IllegalArgumentException("Турнир с названием \"" + request.getName() + "\" уже существует");
    }

    existing.setName(request.getName());
    existing.setStartDate(request.getStartDate());
    existing.setEndDate(request.getEndDate());

    return repository.save(existing);
  }


  @Override
  public void deleteTournament(Long id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("Турнир с id " + id + " не найден");
    }
    repository.deleteById(id);
  }

  @Override
  public TournamentWithTeamsDto getTournamentWithTeams(Long tournamentId) {
    Tournament tournament = repository.findById(tournamentId)
        .orElseThrow(() -> new EntityNotFoundException("Турнир не найден"));

    List<Long> teamIds = tournamentTeamInfoRepository.findByTournamentId(tournamentId)
        .stream()
        .map(TournamentTeamInfo::getTeamId)
        .toList();

    return new TournamentWithTeamsDto(
        tournament.getId(),
        tournament.getName(),
        tournament.getStartDate(),
        tournament.getEndDate(),
        teamIds
    );
  }

}