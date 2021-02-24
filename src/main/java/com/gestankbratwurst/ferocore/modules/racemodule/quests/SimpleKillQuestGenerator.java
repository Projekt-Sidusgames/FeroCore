package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.ThreadLocalRandom;
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
public class SimpleKillQuestGenerator {

  private static final EnumMap<EntityType, Integer[]> POSSIBLE_TARGETS = new EnumMap<>(EntityType.class);

  static {
    POSSIBLE_TARGETS.put(EntityType.BAT, new Integer[]{50, 75, 100});
    POSSIBLE_TARGETS.put(EntityType.CHICKEN, new Integer[]{100, 125, 150, 175, 200});
    POSSIBLE_TARGETS.put(EntityType.COW, new Integer[]{100, 125, 150, 175, 200});
    POSSIBLE_TARGETS.put(EntityType.PIG, new Integer[]{100, 125, 150, 175, 200});
    POSSIBLE_TARGETS.put(EntityType.RABBIT, new Integer[]{50, 75, 100});
    POSSIBLE_TARGETS.put(EntityType.SHEEP, new Integer[]{100, 125, 150, 175, 200});
    POSSIBLE_TARGETS.put(EntityType.STRIDER, new Integer[]{50, 75, 100});
    POSSIBLE_TARGETS.put(EntityType.BEE, new Integer[]{100, 125, 150, 175});
    POSSIBLE_TARGETS.put(EntityType.ENDERMAN, new Integer[]{50, 75, 100, 300});
    POSSIBLE_TARGETS.put(EntityType.SPIDER, new Integer[]{150, 200, 250, 300, 600});
    POSSIBLE_TARGETS.put(EntityType.ZOMBIFIED_PIGLIN, new Integer[]{50, 75, 100});
    POSSIBLE_TARGETS.put(EntityType.CREEPER, new Integer[]{50, 75, 100, 125, 150, 350});
    POSSIBLE_TARGETS.put(EntityType.PILLAGER, new Integer[]{50, 75, 100, 125, 150});
    POSSIBLE_TARGETS.put(EntityType.RAVAGER, new Integer[]{1, 2, 3, 4, 5});
    POSSIBLE_TARGETS.put(EntityType.SKELETON, new Integer[]{150, 200, 250, 300, 600});
    POSSIBLE_TARGETS.put(EntityType.ZOMBIE, new Integer[]{150, 200, 250, 300, 600});
    POSSIBLE_TARGETS.put(EntityType.ZOGLIN, new Integer[]{50, 75, 100});
  }

  public static KillQuest generateSimpleKillQuest(final int objectiveAmounts) {
    final KillQuest killQuest = new KillQuest();
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    final ArrayList<EntityType> types = new ArrayList<>(POSSIBLE_TARGETS.keySet());
    for (int i = 0; i < objectiveAmounts; i++) {
      final EntityType type = types.remove(random.nextInt(types.size()));
      final Integer[] amounts = POSSIBLE_TARGETS.get(type);
      killQuest.addObjective(new SimpleKillObjective(type, amounts[random.nextInt(amounts.length)]));
    }
    return killQuest;
  }

}