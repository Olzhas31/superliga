package com.example.superligasparta.domain.entity;

import com.example.superligasparta.model.enums.CardType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "match_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchCard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "match_id", nullable = false)
  private Long matchId;

  @Column(name = "player_id", nullable = false)
  private Long playerId;

  @Column(nullable = false)
  private Integer minute;

  @Enumerated(EnumType.STRING)
  @Column(name = "card_type", nullable = false)
  private CardType cardType;
}
