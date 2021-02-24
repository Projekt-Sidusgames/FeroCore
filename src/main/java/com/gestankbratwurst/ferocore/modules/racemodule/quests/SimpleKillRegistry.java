package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.bukkit.entity.EntityType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SimpleKillRegistry {

  private static final Map<EntityType, Integer> DIFFICULTY_MAP = ImmutableMap.<EntityType, Integer>builder()
      .put(EntityType.BAT, 13)
      .put(EntityType.CHICKEN, 10)
      .put(EntityType.COW, 10)
      .put(EntityType.PIG, 10)
      .put(EntityType.RABBIT, 14)
      .put(EntityType.SHEEP, 10)
      .put(EntityType.STRIDER, 30)
      .put(EntityType.BEE, 18)
      .put(EntityType.ENDERMAN, 22)
      .put(EntityType.SPIDER, 12)
      .put(EntityType.ZOMBIFIED_PIGLIN, 18)
      .put(EntityType.CREEPER, 18)
      .put(EntityType.PILLAGER, 30)
      .put(EntityType.RAVAGER, 500)
      .put(EntityType.SKELETON, 12)
      .put(EntityType.ZOMBIE, 12)
      .put(EntityType.HOGLIN, 22)
      .build();

  public static int getDifficulty(final EntityType entityType) {
    return DIFFICULTY_MAP.getOrDefault(entityType, 1);
  }

}
