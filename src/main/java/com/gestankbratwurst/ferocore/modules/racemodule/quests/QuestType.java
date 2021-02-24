package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import lombok.RequiredArgsConstructor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public enum QuestType {

  KILL("Töte"),
  GATHER("Sammle");

  private final String displayName;

}
