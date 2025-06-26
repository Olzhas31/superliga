package com.example.superligasparta.mappers;

import com.example.superligasparta.domain.entity.Tournament;
import com.example.superligasparta.model.tournament.CreateTournamentRequest;
import com.example.superligasparta.model.tournament.TournamentDto;

public class TournamentMapper {

  // TODO сделать через builder или mapper
  public static TournamentDto toDto(Tournament entity) {
    return new TournamentDto(
        entity.getId(),
        entity.getName(),
        entity.getStartDate(),
        entity.getEndDate(),
        entity.getArchived()
    );
  }

  public static Tournament toEntity(TournamentDto dto) {
    Tournament tournament = new Tournament();
    tournament.setId(dto.getId());
    tournament.setName(dto.getName());
    tournament.setStartDate(dto.getStartDate());
    tournament.setEndDate(dto.getEndDate());
    tournament.setArchived(dto.getArchived());
    return tournament;
  }

  public static Tournament toEntity(CreateTournamentRequest request) {
    Tournament tournament = new Tournament();
    tournament.setName(request.getName());
    tournament.setStartDate(request.getStartDate());
    tournament.setEndDate(request.getEndDate());
    tournament.setArchived(false);
    return tournament;
  }
}
