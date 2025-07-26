package com.example.superligasparta.domain.entity;

import com.example.superligasparta.model.enums.KitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_kits")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamKit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tournament_team_info_id")
  private Long tournamentTeamInfoId;

  private String color;

  @Enumerated(EnumType.STRING)
  private KitType type;

  @Column(name = "image_url")
  private String imageUrl;
}
