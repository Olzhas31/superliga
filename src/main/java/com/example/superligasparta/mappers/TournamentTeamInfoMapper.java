package com.example.superligasparta.mappers;

import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.model.tournamentTeam.CreateTournamentTeamInfoRequest;
import com.example.superligasparta.model.tournamentTeam.TournamentTeamInfoDto;
import com.example.superligasparta.model.tournamentTeam.UpdateTournamentTeamRequest;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TournamentTeamInfoMapper {

  TournamentTeamInfoDto toDto(TournamentTeamInfo entity);

  List<TournamentTeamInfoDto> toDtoList(List<TournamentTeamInfo> entities);

  TournamentTeamInfo toEntity(CreateTournamentTeamInfoRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateFromRequest(UpdateTournamentTeamRequest request, @MappingTarget TournamentTeamInfo entity);
}
