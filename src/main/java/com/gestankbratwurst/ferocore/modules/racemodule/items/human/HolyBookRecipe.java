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
 * This file is part of FeroCore and was created at the 19.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class HolyBookRecipe extends CustomShapedRecipe {

  public HolyBookRecipe() {
    super("holy_book", HolyBookHandle.createBook());
    super.setShape("ldl", "gbg", "ldl");
    super.setIngredient('l', new ItemStack(Material.GLOWSTONE_DUST), false);
    super.setIngredient('g', new ItemStack(Material.GOLD_INGOT), false);
    super.setIngredient('d', new ItemStack(Material.DIAMOND), false);
    super.setIngredient('b', new ItemStack(Material.BOOK), true);
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
