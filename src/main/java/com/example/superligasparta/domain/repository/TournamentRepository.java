package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

  boolean existsByName(String name);

}
