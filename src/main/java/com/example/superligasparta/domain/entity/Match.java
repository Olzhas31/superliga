package com.example.superligasparta.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.*;


@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long homeTeamId;
  private Long awayTeamId;

  private Integer homeGoals;
  private Integer awayGoals;

  @Column(nullable = false)
  private Boolean played = false;

  private LocalDateTime matchDateTime;

  @Column(name = "tournament_id", nullable = false)
  private Long tournamentId;

  @Column(name = "round_id", nullable = false)
  private Long roundId;

}
