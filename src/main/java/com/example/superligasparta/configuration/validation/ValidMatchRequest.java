package com.example.superligasparta.configuration.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatchRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMatchRequest {
  String message() default "Некорректное состояние: если матч сыгран, нужно указать голы; если не сыгран — голы должны быть null";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
