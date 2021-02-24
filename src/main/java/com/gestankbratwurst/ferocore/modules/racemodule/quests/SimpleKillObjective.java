package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import java.util.ArrayList;
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
public class SimpleKillObjective extends KillQuestObjective {

  public static SimpleKillObjective of(final String data) {
    final String[] split = data.split("##");
    if (split.length != 2) {
      return null;
    }
    final EntityType type = EntityType.valueOf(split[0]);
    final int amount = Integer.parseInt(split[1]);
    return new SimpleKillObjective(type, amount);
  }

  public SimpleKillObjective(final EntityType entityType, final int amount) {
    super(amount, SimpleKillRegistry.getDifficulty(entityType) * amount);
    this.entityType = entityType;
    this.setCondition(living -> living.getType() == this.entityType);
    this.setDisplay("Töte " + entityType.toString());
    this.setDescription(new ArrayList<>());
  }

  private final EntityType entityType;

  @Override
  public String toString() {
    return this.entityType.toString() + "##" + this.getTargetAmount();
  }

}