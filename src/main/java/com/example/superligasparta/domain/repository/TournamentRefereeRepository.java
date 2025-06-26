package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.TournamentReferee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRefereeRepository extends JpaRepository<TournamentReferee, Long> {

  boolean existsByTournamentIdAndRefereeId(Long tournamentId, Long refereeId);

  List<TournamentReferee> findByTournamentId(Long tournamentId);

  void deleteByTournamentIdAndRefereeId(Long tournamentId, Long refereeId);
}

