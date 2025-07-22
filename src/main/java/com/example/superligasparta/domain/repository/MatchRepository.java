package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.Match;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends JpaRepository<Match, Long> {

  List<Match> findAllByTournamentIdAndPlayedTrue(Long tournamentId);

  List<Match> findAllByTournamentId(Long tournamentId);

  List<Match> findAllByRoundId(Long roundId);

  @Query("""
    SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
    FROM Match m
    WHERE m.roundId = :roundId
      AND (m.homeParticipantId = :participantId OR m.awayParticipantId = :participantId)
  """)
  boolean existsByRoundIdAndParticipantId(
      @Param("roundId") Long roundId,
      @Param("participantId") Long participantId
  );

  @Query(value = """
    SELECT EXISTS (
        SELECT 1 FROM matches
        WHERE round_id = :roundId
          AND id <> :matchId
          AND (home_participant_id = :participantId OR away_participant_id = :participantId)
    )
    """, nativeQuery = true)
  boolean existsByRoundIdAndParticipantIdAndNotMatchId(
      @Param("roundId") Long roundId,
      @Param("participantId") Long participantId,
      @Param("matchId") Long matchId
  );

  boolean existsByHomeParticipantIdOrAwayParticipantId(Long homeParticipantId, Long awayParticipantId);


}

