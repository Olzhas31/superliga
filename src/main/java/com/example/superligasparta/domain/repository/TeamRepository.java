package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

  boolean existsByName(String name);
}
