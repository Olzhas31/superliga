package com.example.superligasparta.service.impl;

import com.example.superligasparta.domain.entity.Referee;
import com.example.superligasparta.domain.repository.RefereeRepository;
import com.example.superligasparta.mappers.RefereeMapper;
import com.example.superligasparta.model.referee.RefereeRequest;
import com.example.superligasparta.model.referee.RefereeResponse;
import com.example.superligasparta.service.RefereeService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefereeServiceImpl implements RefereeService {

  private final RefereeRepository refereeRepository;

  @Override
  public RefereeResponse create(RefereeRequest request) {
    Referee saved = refereeRepository.save(RefereeMapper.toEntity(request));
    return RefereeMapper.toResponse(saved);
  }

  @Override
  public RefereeResponse update(Long id, RefereeRequest request) {
    Referee referee = refereeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Судья с id = " + id + " не найден"));
    RefereeMapper.updateEntity(referee, request);
    return RefereeMapper.toResponse(refereeRepository.save(referee));
  }

  @Override
  public void delete(Long id) {
    if (!refereeRepository.existsById(id)) {
      throw new EntityNotFoundException("Судья с id = " + id + " не найден");
    }
    refereeRepository.deleteById(id);
  }

  @Override
  public RefereeResponse getById(Long id) {
    return refereeRepository.findById(id)
        .map(RefereeMapper::toResponse)
        .orElseThrow(() -> new EntityNotFoundException("Судья с id = " + id + " не найден"));
  }

  @Override
  public List<RefereeResponse> getAll() {
    return refereeRepository.findAll()
        .stream()
        .map(RefereeMapper::toResponse)
        .collect(Collectors.toList());
  }
}


