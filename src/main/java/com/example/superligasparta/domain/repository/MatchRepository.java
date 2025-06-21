package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.Match;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends JpaRepository<Match, Long> {

  List<Match> findAllByPlayedTrue();

  List<Match> findAllByTournamentIdAndPlayedTrue(Long tournamentId);

  List<Match> findAllByTournamentId(Long tournamentId);

  @Query("""
    SELECT COUNT(m) > 0
    FROM Match m
    WHERE m.tournamentId = :tournamentId
      AND (m.homeTeamId = :teamId OR m.awayTeamId = :teamId)
  """)
  boolean existsByTournamentIdAndTeamId(@Param("tournamentId") Long tournamentId,
      @Param("teamId") Long teamId);

  @Query("""
    SELECT COUNT(m) > 0 FROM Match m
    WHERE m.roundId = :roundId AND (m.homeTeamId = :teamId OR m.awayTeamId = :teamId)
  """)
  boolean existsByRoundIdAndTeamId(@Param("roundId") Long roundId, @Param("teamId") Long teamId);

  @Query("""
    SELECT COUNT(m) > 0 FROM Match m
    WHERE m.roundId = :roundId
      AND (m.homeTeamId = :teamId OR m.awayTeamId = :teamId)
      AND m.id <> :matchId
  """)
  boolean existsByRoundIdAndTeamIdAndNotMatchId(Long roundId, Long teamId, Long matchId);

}

