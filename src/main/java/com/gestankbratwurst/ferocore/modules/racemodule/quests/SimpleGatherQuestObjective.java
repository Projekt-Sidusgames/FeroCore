package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import java.util.ArrayList;
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
public class SimpleGatherQuestObjective extends GatherQuestObjective {

  public static SimpleGatherQuestObjective of(final String data) {
    final String[] split = data.split("##");
    if (split.length != 2) {
      return null;
    }
    final Material material = Material.valueOf(split[0]);
    final int amount = Integer.parseInt(split[1]);
    return new SimpleGatherQuestObjective(material, amount);
  }

  public SimpleGatherQuestObjective(final Material material, final int amount) {
    super(amount, SimpleGatherRegistry.getDifficulty(material) * amount);
    this.material = material;
    super.setCondition(item -> item.getItemStack().getType() == this.material);
    this.setDisplay("Sammle " + material.toString());
    this.setDescription(new ArrayList<>());
  }

  private final Material material;

  @Override
  public String toString() {
    return this.material.toString() + "##" + this.getTargetAmount();
  }

}
