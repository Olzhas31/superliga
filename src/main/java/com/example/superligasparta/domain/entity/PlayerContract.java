package com.example.superligasparta.domain.entity;

import com.example.superligasparta.model.enums.PlayerPosition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_contracts",
    uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "tournament_team_info_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerContract {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "player_id", nullable = false)
  private Long playerId;

  @Column(name = "tournament_team_info_id", nullable = false)
  private Long tournamentTeamInfoId;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @NotNull
  @Column(name = "shirt_number", nullable = false)
  private Integer shirtNumber;

  @NotNull
  @Enumerated(EnumType.STRING)
  private PlayerPosition position;

  public boolean isActive() {
    return endDate == null || endDate.isAfter(LocalDate.now());
  }
}

