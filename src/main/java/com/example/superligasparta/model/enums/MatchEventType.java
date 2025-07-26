package com.example.superligasparta.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum MatchEventType {

  GOAL("гол", MatchEventCategory.GOAL),
  OWN_GOAL("автогол", MatchEventCategory.GOAL),
  PENALTY("гол с пенальти", MatchEventCategory.GOAL),
  YELLOW("желтая карточка", MatchEventCategory.CARD),
  RED("красная карточка", MatchEventCategory.CARD);

  private String name;
  @Getter
  private MatchEventCategory category;
}
