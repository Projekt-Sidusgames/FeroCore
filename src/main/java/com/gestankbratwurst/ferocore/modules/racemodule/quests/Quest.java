package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface Quest {

  double getProgressPercent();

  String getName();

  List<String> getDescription();

  ItemStack getIcon();

  int getRewardPoints();

  void addProgressBarView(Player player);

}