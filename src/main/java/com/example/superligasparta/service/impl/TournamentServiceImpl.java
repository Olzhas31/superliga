package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Tournament;
import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.TeamRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.mappers.TournamentMapper;
import com.example.superligasparta.model.tournament.CreateTournamentRequest;
import com.example.superligasparta.model.tournament.TournamentDto;
import com.example.superligasparta.model.tournament.TournamentWithTeamsDto;
import com.example.superligasparta.model.tournament.UpdateTournamentRequest;
import com.example.superligasparta.service.TournamentService;
import com.example.superligasparta.validation.EntityValidator;
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
  private final EntityValidator entityValidator;

  @Override
  public List<Tournament> getAllTournaments() {
    return repository.findAll();
  }

  @Override
  public TournamentDto getTournamentById(Long id) {
    entityValidator.validateTournamentExists(id);
    Tournament tournament = repository.findById(id).get();
    return TournamentMapper.toDto(tournament);
  }

  @Override
  public TournamentDto createTournament(CreateTournamentRequest request) {
    entityValidator.validateTournamentNameIsUnique(request.getName());
    Tournament tournament = repository.save(TournamentMapper.toEntity(request));
    return TournamentMapper.toDto(tournament);
  }

  @Override
  public TournamentDto updateTournament(Long id, UpdateTournamentRequest request) {
    entityValidator.validateTournamentExists(id);
    // TODO если изменю только время ошибка будет
    entityValidator.validateTournamentNameIsUnique(request.getName());
    Tournament tournament = repository.findById(id).get();

    tournament.setName(request.getName());
    tournament.setStartDate(request.getStartDate());
    tournament.setEndDate(request.getEndDate());
    tournament.setArchived(request.getArchived());

    return TournamentMapper.toDto(repository.save(tournament));
  }


  @Override
  public void deleteTournament(Long id) {
    entityValidator.validateTournamentExists(id);
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