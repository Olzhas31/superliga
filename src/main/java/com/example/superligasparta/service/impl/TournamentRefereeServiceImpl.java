package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.TournamentReferee;
import com.example.superligasparta.domain.repository.RefereeRepository;
import com.example.superligasparta.domain.repository.TournamentRefereeRepository;
import com.example.superligasparta.mappers.RefereeMapper;
import com.example.superligasparta.model.referee.RefereeResponse;
import com.example.superligasparta.service.TournamentRefereeService;
import com.example.superligasparta.validation.EntityValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TournamentRefereeServiceImpl implements TournamentRefereeService {

  private final TournamentRefereeRepository tournamentRefereeRepository;
  private final RefereeRepository refereeRepository;
  private final EntityValidator entityValidator;

  @Override
  public void assignReferee(Long tournamentId, Long refereeId) {
    entityValidator.validateTournamentExists(tournamentId);
    entityValidator.validateRefereeExists(refereeId);
    entityValidator.validateRefereeNotAlreadyAssigned(tournamentId, refereeId);

    TournamentReferee tr = TournamentReferee.builder()
        .refereeId(refereeId)
        .tournamentId(tournamentId)
        .build();
    tournamentRefereeRepository.save(tr);
  }

  @Override
  @Transactional
  public void removeReferee(Long tournamentId, Long refereeId) {
    entityValidator.validateTournamentExists(tournamentId);
    entityValidator.validateRefereeExists(refereeId);
    entityValidator.validateRefereeAssignmentExists(tournamentId, refereeId);
    tournamentRefereeRepository.deleteByTournamentIdAndRefereeId(tournamentId, refereeId);
  }

  @Override
  public List<RefereeResponse> getRefereesByTournament(Long tournamentId) {
    entityValidator.validateTournamentExists(tournamentId);
    List<TournamentReferee> links = tournamentRefereeRepository.findByTournamentId(tournamentId);

    List<Long> refereeIds = links.stream()
        .map(TournamentReferee::getRefereeId)
        .toList();

    return refereeRepository.findAllById(refereeIds).stream()
        .map(RefereeMapper::toResponse)
        .toList();
  }
}