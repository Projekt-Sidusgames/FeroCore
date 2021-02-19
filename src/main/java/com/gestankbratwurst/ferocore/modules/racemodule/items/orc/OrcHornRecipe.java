package com.gestankbratwurst.ferocore.modules.racemodule.items.orc;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 18.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class OrcHornRecipe extends CustomShapedRecipe {

  public OrcHornRecipe() {
    super("orc_horn", OrcHornItemHandle.createHorn());
    super.setShape("bbb", "ldl", "bbb");
    super.setIngredient('b', new ItemStack(Material.BONE), false);
    super.setIngredient('l', new ItemStack(Material.LEATHER), false);
    super.setIngredient('d', new ItemStack(Material.SKELETON_SKULL), false);
  }

  @Override
  public boolean canCraft(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    return feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.ORC;
  }

  @Override
  public void onCraft(final Player player, final int amount) {

  }
}
