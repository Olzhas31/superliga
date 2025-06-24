package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
