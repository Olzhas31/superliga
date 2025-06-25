package com.example.superligasparta.mappers;

import com.example.superligasparta.domain.entity.Referee;
import com.example.superligasparta.model.referee.RefereeRequest;
import com.example.superligasparta.model.referee.RefereeResponse;

public class RefereeMapper {

  public static Referee toEntity(RefereeRequest request) {
    Referee r = new Referee();
    r.setName(request.getName());
    r.setSurname(request.getSurname());
    r.setFathersName(request.getFathersName());
    return r;
  }

  public static RefereeResponse toResponse(Referee referee) {
    return new RefereeResponse(
        referee.getId(),
        referee.getName(),
        referee.getSurname(),
        referee.getFathersName()
    );
  }

  public static void updateEntity(Referee referee, RefereeRequest request) {
    referee.setName(request.getName());
    referee.setSurname(request.getSurname());
    referee.setFathersName(request.getFathersName());
  }
}

