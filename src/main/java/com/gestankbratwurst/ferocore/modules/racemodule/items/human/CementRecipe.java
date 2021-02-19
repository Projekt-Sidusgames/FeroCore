package com.gestankbratwurst.ferocore.modules.racemodule.items.human;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 15.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CementRecipe extends CustomShapedRecipe {

  public CementRecipe() {
    super("cement", CementItemHandle.createCement(16));
    super.setShape("ccc", "dwc", "ddd");
    super.setIngredient('c', new ItemStack(Material.CLAY_BALL), false);
    super.setIngredient('w', new ItemStack(Material.WATER_BUCKET));
    super.setIngredient('d', new ItemStack(Material.DIORITE), false);
  }

  @Override
  public boolean canCraft(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    return feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.HUMAN;
  }

  @Override
  public void onCraft(final Player player, final int amount) {

  }
}
