package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.MatchCard;
import com.example.superligasparta.model.enums.CardType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchCardRepository extends JpaRepository<MatchCard, Long> {

  // Найти все карточки по матчу
  List<MatchCard> findAllByMatchId(Long matchId);

  // Найти все карточки по списку матчей (например, для турнира)
  List<MatchCard> findAllByMatchIdIn(List<Long> matchIds);

  // Найти карточки конкретного игрока
  List<MatchCard> findAllByPlayerId(Long playerId);

  // Количество карточек по игроку и типу (например, желтые карточки)
  long countByPlayerIdAndCardType(Long playerId, CardType cardType);

  // Количество карточек игрока в конкретном турнире
  @Query("""
    SELECT COUNT(c) 
    FROM MatchCard c
    WHERE c.cardType = :cardType
      AND c.playerId = :playerId
      AND c.matchId IN (
        SELECT m.id FROM Match m WHERE m.tournamentId = :tournamentId
      )
    """)
  long countByTournamentAndPlayer(@Param("tournamentId") Long tournamentId,
      @Param("playerId") Long playerId,
      @Param("cardType") CardType cardType);

  // Список всех карточек игрока в турнире
  @Query("""
    SELECT c 
    FROM MatchCard c
    WHERE c.playerId = :playerId
      AND c.matchId IN (
        SELECT m.id FROM Match m WHERE m.tournamentId = :tournamentId
      )
    """)
  List<MatchCard> findByTournamentAndPlayer(@Param("tournamentId") Long tournamentId,
      @Param("playerId") Long playerId);
}

