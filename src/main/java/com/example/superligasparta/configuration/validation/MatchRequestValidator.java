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
    Boolean played;
    Integer homeGoals;
    Integer awayGoals;

    if (obj instanceof CreateMatchRequest dto) {
      homeTeamId = dto.getHomeTeamId();
      awayTeamId = dto.getAwayTeamId();
      played = dto.getPlayed();
      homeGoals = dto.getHomeGoals();
      awayGoals = dto.getAwayGoals();
    } else {
      UpdateMatchRequest dto = (UpdateMatchRequest) obj;
      homeTeamId = dto.getHomeTeamId();
      awayTeamId = dto.getAwayTeamId();
      played = dto.getPlayed();
      homeGoals = dto.getHomeGoals();
      awayGoals = dto.getAwayGoals();
    }

    context.disableDefaultConstraintViolation();
    boolean valid = true;

    if (homeTeamId != null && awayTeamId != null && homeTeamId.equals(awayTeamId)) {
      context.buildConstraintViolationWithTemplate("Команда не может играть сама с собой")
          .addConstraintViolation();
      valid = false;
    }

    if (played != null) {
      if (played) {
        if (homeGoals == null || awayGoals == null) {
          context.buildConstraintViolationWithTemplate("Если матч сыгран, необходимо указать счёт")
              .addConstraintViolation();
          valid = false;
        }
      } else {
        if (homeGoals != null || awayGoals != null) {
          context.buildConstraintViolationWithTemplate("Если матч ещё не сыгран, счёт должен быть пустым")
              .addConstraintViolation();
          valid = false;
        }
      }
    }

    return valid;
  }
}

