package com.gestankbratwurst.ferocore.modules.customtiles;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import lombok.Getter;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomTileModule implements BaseModule {

  @Getter
  private TileManager tileManager;

  @Override
  public void enable(final FeroCore plugin) {
    this.tileManager = new TileManager(plugin);
    TaskManager.getInstance().runBukkitSyncDelayed(() -> Bukkit.getWorlds().forEach(this.tileManager::initWorld), 60L);
  }

  @Override
  public void disable(final FeroCore plugin) {

  }

}
