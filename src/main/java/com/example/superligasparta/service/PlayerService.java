package com.example.superligasparta.service;

import com.example.superligasparta.domain.entity.Player;
import com.example.superligasparta.model.player.CreatePlayerRequest;
import com.example.superligasparta.model.player.UpdatePlayerRequest;
import java.util.List;

public interface PlayerService {

  Player create(CreatePlayerRequest request);

  Player update(Long id, UpdatePlayerRequest request);

  Player getById(Long id);

  List<Player> getAll();

  List<Player> getAllByIds(List<Long> ids);

  void delete(Long id);
}
