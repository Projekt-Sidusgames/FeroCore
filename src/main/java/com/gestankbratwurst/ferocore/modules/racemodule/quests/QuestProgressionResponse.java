package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum QuestProgressionResponse {

  NONE(false),
  COMPLETE(true),
  PROGRESS(true),
  NO_PROGRESS(false);

  @Getter
  private final boolean progressShowCause;

}
