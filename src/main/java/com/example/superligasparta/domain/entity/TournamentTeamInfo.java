package com.example.superligasparta.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tournament_team_info",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tournament_id", "team_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentTeamInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tournament_id", nullable = false)
  private Long tournamentId;

  @Column(name = "team_id", nullable = false)
  private Long teamId;

  @Column(nullable = false)
  private String displayName;

  @Column(name = "captain_contract_id")
  private Long captainContractId;

  @Column(name = "logo_url")
  private String logoUrl;
}
