package com.example.superligasparta.configuration.validation;

import com.example.superligasparta.model.match.CreateMatchRequest;
import com.example.superligasparta.model.match.UpdateMatchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class MatchRequestValidator implements ConstraintValidator<ValidMatchRequest, Object> {

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    // Безопасная проверка: obj должен быть CreateMatchDto или UpdateMatchRequest
    if (!(obj instanceof CreateMatchRequest || obj instanceof UpdateMatchRequest)) {
      return true;
    }

    Long homeTeamId;
    Long awayTeamId;

    if (obj instanceof CreateMatchRequest dto) {
      homeTeamId = dto.getHomeParticipantId();
      awayTeamId = dto.getAwayParticipantId();
    } else {
      UpdateMatchRequest dto = (UpdateMatchRequest) obj;
      homeTeamId = dto.getHomeParticipantId();
      awayTeamId = dto.getAwayParticipantId();
    }

    context.disableDefaultConstraintViolation();
    boolean valid = true;

    if (homeTeamId != null && awayTeamId != null && homeTeamId.equals(awayTeamId)) {
      context.buildConstraintViolationWithTemplate("Команда не может играть сама с собой")
          .addConstraintViolation();
      valid = false;
    }
    return valid;
  }
}

