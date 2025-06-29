package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.PlayerContract;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerContractRepository extends JpaRepository<PlayerContract, Long> {

  @Query("""
    SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END
    FROM PlayerContract pc
    WHERE pc.playerId = :playerId
      AND pc.tournamentTeamInfoId = :teamInfoId
      AND (
           (pc.endDate IS NULL OR pc.endDate >= :startDate)
        AND pc.startDate <= COALESCE(:endDate, CURRENT_DATE)
      )
  """)
  boolean existsConflictingContract(
      @Param("playerId") Long playerId,
      @Param("teamInfoId") Long tournamentTeamInfoId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate
  );

//  List<PlayerContract> findByPlayerIdAndTournamentId(Long playerId, Long tournamentId);

  List<PlayerContract> findByPlayerId(Long playerId);

//  boolean existsByPlayerIdAndTournamentIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
//      Long playerId, Long tournamentId, LocalDate date1, LocalDate date2);

  @Query("""
    SELECT COUNT(pc) > 0
    FROM PlayerContract pc
    JOIN TournamentTeamInfo tti ON pc.tournamentTeamInfoId = tti.id
    WHERE pc.playerId = :playerId
      AND tti.tournamentId = :tournamentId
      AND pc.tournamentTeamInfoId <> :currentTeamInfoId
      AND (
           (pc.endDate IS NULL OR pc.endDate >= :startDate)
        AND pc.startDate <= COALESCE(:endDate, CURRENT_DATE)
      )
  """)
  boolean existsInAnotherTeam(
      @Param("playerId") Long playerId,
      @Param("tournamentId") Long tournamentId,
      @Param("currentTeamInfoId") Long currentTeamInfoId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate
  );

  @Query("""
    SELECT COUNT(pc) > 0
    FROM PlayerContract pc
    WHERE pc.tournamentTeamInfoId = :teamInfoId
      AND pc.shirtNumber = :shirtNumber
      AND (
           (pc.endDate IS NULL OR pc.endDate >= :startDate)
        AND pc.startDate <= COALESCE(:endDate, CURRENT_DATE)
      )
  """)
  boolean isShirtNumberTaken(
      @Param("teamInfoId") Long tournamentTeamInfoId,
      @Param("shirtNumber") Integer shirtNumber,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate
  );

  @Query("SELECT pc FROM PlayerContract pc WHERE pc.playerId = :playerId AND pc.tournamentTeamInfoId IN :participantIds")
  List<PlayerContract> findByPlayerIdAndTournamentTeamInfoIdIn(@Param("playerId") Long playerId,
      @Param("participantIds") List<Long> participantIds);


}

