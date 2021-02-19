package com.gestankbratwurst.ferocore.modules.skillmodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import org.bukkit.Bukkit;

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

  private final SkillTask skillTask = new SkillTask();

  public void addTickableSkill(final TickableSkill tickableSkill) {
    this.skillTask.addSkill(tickableSkill);
  }

  @Override
  public void enable(final FeroCore plugin) {
    Bukkit.getScheduler().runTaskTimer(plugin, this.skillTask, 1, 1);
  }

  @Override
  public void disable(final FeroCore plugin) {

  }
}
