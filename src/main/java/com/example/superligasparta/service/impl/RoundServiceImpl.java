package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Round;
import com.example.superligasparta.domain.repository.RoundRepository;
import com.example.superligasparta.domain.repository.TournamentRepository;
import com.example.superligasparta.model.round.CreateRoundRequest;
import com.example.superligasparta.service.RoundService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

  private final RoundRepository roundRepository;
  private final TournamentRepository tournamentRepository;

  @Override
  public Round create(CreateRoundRequest request) {
    if (!tournamentRepository.existsById(request.getTournamentId())) {
      throw new EntityNotFoundException("Турнир с id " + request.getTournamentId() + " не найден");
    }

    boolean nameExists = roundRepository
        .existsByTournamentIdAndName(request.getTournamentId(), request.getName());

    if (nameExists) {
      throw new IllegalArgumentException("Тур с таким названием уже существует в этом турнире");
    }

    Round round = new Round();
    round.setName(request.getName());
    round.setTournamentId(request.getTournamentId());

    return roundRepository.save(round);
  }

  @Override
  public List<Round> getByTournament(Long tournamentId) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new EntityNotFoundException("Турнир с id " + tournamentId + " не найден");
    }
    return roundRepository.findAllByTournamentId(tournamentId);
  }
}
