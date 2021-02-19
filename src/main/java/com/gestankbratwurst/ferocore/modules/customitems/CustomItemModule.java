package com.gestankbratwurst.ferocore.modules.customitems;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomItemModule implements BaseModule {

  @Getter
  private CustomItemManager customItemManager;

  @Override
  public void enable(final FeroCore plugin) {
    this.customItemManager = new CustomItemManager();
    FeroCore.registerListener(new CustomItemListener(this.customItemManager));
    // Bukkit.getScheduler().runTaskTimer(plugin, new CustomItemRunnable(this.customItemManager), 1L, 1L);
  }

  @Override
  public void disable(final FeroCore plugin) {

  }

}