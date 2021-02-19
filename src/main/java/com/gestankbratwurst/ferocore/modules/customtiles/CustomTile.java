package com.gestankbratwurst.ferocore.modules.customtiles;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface CustomTile {

  void tick();

  void save(PersistentDataContainer pdc, long currentTime);

  void load(PersistentDataContainer pdc, long deltaTime);

  void onBreak(BlockBreakEvent event);

}