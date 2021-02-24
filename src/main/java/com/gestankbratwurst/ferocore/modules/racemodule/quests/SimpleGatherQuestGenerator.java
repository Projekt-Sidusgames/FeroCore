package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SimpleGatherQuestGenerator {

  private static final EnumMap<Material, Integer[]> POSSIBLE_BASE_TARGETS = new EnumMap<>(Material.class);
  private static final EnumMap<RaceType, EnumMap<Material, Integer[]>> POSSIBLE_RACE_TARGETS = new EnumMap<>(RaceType.class);

  static {
    POSSIBLE_BASE_TARGETS.put(Material.COBBLESTONE, new Integer[]{2500, 3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500, 10000, 20000});
    POSSIBLE_BASE_TARGETS.put(Material.NETHERRACK, new Integer[]{2500, 3000, 3500, 4000, 4500, 5000});
    POSSIBLE_BASE_TARGETS.put(Material.IRON_ORE, new Integer[]{150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400});
    POSSIBLE_BASE_TARGETS.put(Material.COAL, new Integer[]{200, 250, 300, 350, 400, 450, 500});
    POSSIBLE_BASE_TARGETS.put(Material.DIAMOND, new Integer[]{30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150});
    POSSIBLE_BASE_TARGETS.put(Material.REDSTONE, new Integer[]{200, 300, 400, 500, 600, 700, 800, 900, 1000});
    POSSIBLE_BASE_TARGETS.put(Material.LAPIS_LAZULI, new Integer[]{100, 125, 150, 175, 200, 225, 250});
    POSSIBLE_BASE_TARGETS.put(Material.GOLD_ORE, new Integer[]{50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150});
    for (final RaceType raceType : RaceType.values()) {
      POSSIBLE_RACE_TARGETS.put(raceType, POSSIBLE_BASE_TARGETS.clone());
    }
  }

  public static GatherQuest generateSimpleKillQuest(final int objectiveAmounts, final RaceType raceType) {
    final GatherQuest gatherQuest = new GatherQuest();
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    final ArrayList<Material> types = new ArrayList<>(POSSIBLE_RACE_TARGETS.get(raceType).keySet());
    for (int i = 0; i < objectiveAmounts; i++) {
      final Material type = types.remove(random.nextInt(types.size()));
      final Integer[] amounts = POSSIBLE_RACE_TARGETS.get(raceType).get(type);
      gatherQuest.addObjective(new SimpleGatherQuestObjective(type, amounts[random.nextInt(amounts.length)]));
    }
    return gatherQuest;
  }
}
