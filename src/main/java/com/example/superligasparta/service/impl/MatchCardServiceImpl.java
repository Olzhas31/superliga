package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.repository.MatchCardRepository;
import com.example.superligasparta.service.MatchCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchCardServiceImpl implements MatchCardService {

  private final MatchCardRepository matchCardRepository;

  @Override
  public void deleteById(Long id) {
    matchCardRepository.deleteById(id);
  }
}
