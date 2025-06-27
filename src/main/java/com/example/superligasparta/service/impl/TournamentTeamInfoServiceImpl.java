package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.domain.repository.TournamentTeamInfoRepository;
import com.example.superligasparta.mappers.TournamentTeamInfoMapper;
import com.example.superligasparta.model.tournamentTeam.CreateTournamentTeamInfoRequest;
import com.example.superligasparta.model.tournamentTeam.TournamentTeamInfoDto;
import com.example.superligasparta.model.tournamentTeam.UpdateTournamentTeamRequest;
import com.example.superligasparta.service.TournamentTeamInfoService;
import com.example.superligasparta.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentTeamInfoServiceImpl implements TournamentTeamInfoService {
  private final TournamentTeamInfoRepository tournamentTeamInfoRepository;
  private final EntityValidator entityValidator;
  private final TournamentTeamInfoMapper mapper;

  @Override
  public Long create(CreateTournamentTeamInfoRequest request) {
    entityValidator.validateTournamentExists(request.getTournamentId());
    entityValidator.validateTeamExists(request.getTeamId());
    entityValidator.validateTeamNotAlreadyInTournament(request.getTournamentId(), request.getTeamId());
    entityValidator.validateDisplayNameIsUniqueInTournament(request.getTournamentId(), request.getDisplayName());
    TournamentTeamInfo entity = mapper.toEntity(request);
    return tournamentTeamInfoRepository.save(entity).getId();
  }

  @Override
  public TournamentTeamInfoDto getById(Long id) {
    return tournamentTeamInfoRepository.findById(id)
        .map(mapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("Участник турнира с id = " + id + " не найден"));
  }

  @Override
  public void update(Long id, UpdateTournamentTeamRequest request) {
    entityValidator.validateTournamentExists(request.getTournamentId());
    entityValidator.validateTeamExists(request.getTeamId());
    // TODO если не меняется не нужно проверка
    entityValidator.validateDisplayNameIsUniqueInTournament(request.getTournamentId(), request.getDisplayName());
    entityValidator.validateTournamentTeamInfoExists(id);
    //entityValidator.validateContractBelongsToTournamentTeamInfo(captainContractId, tournamentTeamInfoId);

    TournamentTeamInfo entity = tournamentTeamInfoRepository.findById(id).get();
    mapper.updateFromRequest(request, entity);
    tournamentTeamInfoRepository.save(entity);
  }

  @Override
  public void delete(Long id) {
    entityValidator.validateTournamentTeamInfoExists(id);
    // TODO если были сыгранные игры не удалять
//    boolean hasMatches = matchRepository.existsByHomeParticipantIdOrAwayParticipantId(participantId, participantId);
//    if (hasMatches) {
//      throw new IllegalStateException("Нельзя удалить команду — в турнире уже есть матчи с её участием");
//    }
    tournamentTeamInfoRepository.deleteById(id);
  }

  @Override
  public List<TournamentTeamInfoDto> getByTournamentId(Long tournamentId) {
    return mapper.toDtoList(tournamentTeamInfoRepository.findByTournamentId(tournamentId));
  }
}