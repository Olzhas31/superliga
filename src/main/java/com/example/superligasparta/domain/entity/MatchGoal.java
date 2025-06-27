package com.example.superligasparta.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "match_goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchGoal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "match_id", nullable = false)
  private Long matchId;

  @Column(name = "player_id", nullable = false)
  private Long playerId;

  @Column
  private Integer minute;

  @Column(name = "is_own_goal", nullable = false)
  private boolean isOwnGoal = false;

  @Column(name = "is_penalty", nullable = false)
  private boolean isPenalty = false;
}

