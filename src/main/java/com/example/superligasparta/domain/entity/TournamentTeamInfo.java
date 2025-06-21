package com.example.superligasparta.domain.entity;

import com.example.superligasparta.model.TournamentTeamId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tournament_team_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TournamentTeamId.class)
public class TournamentTeamInfo {

  @Id
  private Long tournamentId;

  @Id
  private Long teamId;

  @Column(nullable = false)
  private String displayName;

  // В будущем можно добавить логотип, форму, цвета и т.п.
}
