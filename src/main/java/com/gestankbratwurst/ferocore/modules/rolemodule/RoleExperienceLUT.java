package com.gestankbratwurst.ferocore.modules.rolemodule;

import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 24.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RoleExperienceLUT {

  private static final Int2LongMap LEVEL_TO_EXPERIENCE_TABLE = new Int2LongOpenHashMap();

  static {
    for (int lvl = 2; lvl < 100; lvl++) {
      LEVEL_TO_EXPERIENCE_TABLE.put(lvl, 50 + (lvl - 1) * 150 + lvl * lvl * 12);
    }
    LEVEL_TO_EXPERIENCE_TABLE.put(1, 0);
  }

  public static long getTotalExperienceToLevel(final int level) {
    return LEVEL_TO_EXPERIENCE_TABLE.get(level - 1);
  }

  public static long getExperienceFromTo(final int lvlFrom, final int toLevel) {
    return getTotalExperienceToLevel(toLevel) - getTotalExperienceToLevel(lvlFrom);
  }

  public static int getLevelOf(final long currentExperience) {
    for (int lvl = 1; lvl < LEVEL_TO_EXPERIENCE_TABLE.size(); lvl++) {
      if (LEVEL_TO_EXPERIENCE_TABLE.get(lvl) > currentExperience) {
        return lvl - 1;
      }
    }
    return 1;
  }

  public static int getLevelUps(final int currentLevel, final long currentExperience) {
    int lvl = currentLevel + 1;
    int lvls = 0;
    while (getTotalExperienceToLevel(lvl++) < currentExperience) {
      lvls++;
    }
    return lvls;
  }

}
