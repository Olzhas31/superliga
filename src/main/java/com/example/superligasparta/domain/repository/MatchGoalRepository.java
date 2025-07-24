package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.MatchGoal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchGoalRepository extends JpaRepository<MatchGoal, Long> {

  @Query("SELECT g FROM MatchGoal g WHERE g.matchId IN :matchIds")
  List<MatchGoal> findByMatchIds(@Param("matchIds") List<Long> matchIds);

  List<MatchGoal> findAllByMatchId(Long matchId);
}

