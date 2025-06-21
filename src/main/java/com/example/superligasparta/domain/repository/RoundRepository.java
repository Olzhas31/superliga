package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.Round;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

  List<Round> findAllByTournamentId(Long tournamentId);

  boolean existsByTournamentIdAndName(Long tournamentId, String name);
}
