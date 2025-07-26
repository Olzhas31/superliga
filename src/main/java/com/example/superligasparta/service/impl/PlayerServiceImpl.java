package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Player;
import com.example.superligasparta.domain.repository.PlayerRepository;
import com.example.superligasparta.model.player.CreatePlayerRequest;
import com.example.superligasparta.model.player.UpdatePlayerRequest;
import com.example.superligasparta.service.PlayerService;
import com.example.superligasparta.validation.EntityValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepository;
  private final EntityValidator entityValidator;

  @Override
  public Player create(CreatePlayerRequest request) {
    Player player = Player.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .birthDate(request.getBirthDate())
        .nickname(request.getNickname())
        .build();
    return playerRepository.save(player);
  }

  @Override
  public Player update(Long id, UpdatePlayerRequest request) {
    entityValidator.validatePlayerExists(id);
    Player player = playerRepository.findById(id).get();
    player.setName(request.getName());
    player.setSurname(request.getSurname());
    player.setBirthDate(request.getBirthDate());
    player.setNickname(request.getNickname());

    return playerRepository.save(player);
  }

  @Override
  public Player getById(Long id) {
    entityValidator.validatePlayerExists(id);
    return playerRepository.findById(id).get();
  }

  @Override
  public List<Player> getAll() {
    return playerRepository.findAll();
  }

  @Override
  public List<Player> getAllByIds(List<Long> ids) {
    return playerRepository.findAllById(ids);
  }

  @Override
  public void delete(Long id) {
    entityValidator.validatePlayerExists(id);
    playerRepository.deleteById(id);
  }
}
