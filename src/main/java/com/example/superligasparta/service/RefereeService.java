package com.example.superligasparta.service;

import com.example.superligasparta.model.referee.RefereeRequest;
import com.example.superligasparta.model.referee.RefereeResponse;
import java.util.List;

public interface RefereeService {

  RefereeResponse create(RefereeRequest request);

  RefereeResponse update(Long id, RefereeRequest request);

  void delete(Long id);

  RefereeResponse getById(Long id);

  List<RefereeResponse> getAll();
}

