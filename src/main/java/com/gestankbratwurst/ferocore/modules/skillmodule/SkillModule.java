package com.gestankbratwurst.ferocore.modules.skillmodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 17.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SkillModule implements BaseModule {

  private static final SkillTask skillTask = new SkillTask();

  public static void setCooldown(final Player player, final String key, final long millis) {
    player.getPersistentDataContainer()
        .set(NameSpaceFactory.provide("Skill_CD_" + key), PersistentDataType.LONG, System.currentTimeMillis() + millis);
  }

  public static long getCooldownLeft(final Player player, final String key) {
    final Long value = player.getPersistentDataContainer()
        .get(NameSpaceFactory.provide("Skill_CD_" + key), PersistentDataType.LONG);
    return value == null ? 0L : value - System.currentTimeMillis();
  }

  public static void addTickableSkill(final TickableSkill tickableSkill) {
    skillTask.addSkill(tickableSkill);
  }

  @Override
  public void enable(final FeroCore plugin) {
    Bukkit.getScheduler().runTaskTimer(plugin, skillTask, 1, 1);
  }

  @Override
  public void disable(final FeroCore plugin) {

  }
}
