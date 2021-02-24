package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
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
public class SimpleGatherRegistry {

  private static final Map<Material, Integer> DIFFICULTY_MAP = ImmutableMap.<Material, Integer>builder()
      .put(Material.COBBLESTONE, 1)
      .put(Material.NETHERRACK, 2)
      .put(Material.IRON_ORE, 5)
      .put(Material.COAL, 3)
      .put(Material.DIAMOND, 20)
      .put(Material.REDSTONE, 3)
      .put(Material.LAPIS_LAZULI, 16)
      .put(Material.GOLD_ORE, 15)
      .build();

  public static int getDifficulty(final Material material) {
    return DIFFICULTY_MAP.getOrDefault(material, 1);
  }

}
