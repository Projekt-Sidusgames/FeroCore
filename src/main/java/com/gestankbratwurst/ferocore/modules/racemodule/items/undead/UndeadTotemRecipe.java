package com.gestankbratwurst.ferocore.modules.racemodule.items.undead;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 17.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UndeadTotemRecipe extends CustomShapedRecipe {

  public UndeadTotemRecipe() {
    super("undead_totem", UndeadTotemHandle.createTotem());
    super.setShape(" s ", "cgc", "cbc");
    super.setIngredient('s', new ItemStack(Material.SKELETON_SKULL), false);
    super.setIngredient('c', new ItemStack(Material.CRIMSON_STEM), false);
    super.setIngredient('g', new ItemStack(Material.GHAST_TEAR), false);
    super.setIngredient('b', new ItemStack(Material.BASALT), false);
  }

  @Override
  public boolean canCraft(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    return feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.UNDEAD;
  }

  @Override
  public void onCraft(final Player player, final int amount) {

  }
}
