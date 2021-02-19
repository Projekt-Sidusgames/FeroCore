package com.gestankbratwurst.ferocore.modules.racemodule.items.dwarf;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.modules.racemodule.items.dwarf.DwarfCanonHandle.CanonBallType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 07.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CanonBallRecipe extends CustomShapedRecipe {

  public CanonBallRecipe() {
    super("canon_ball", CanonBallType.NORMAL.getAsItem());
    this.setShape(" i ", "igi", " i ");
    this.setIngredient('i', new ItemStack(Material.IRON_INGOT));
    this.setIngredient('g', new ItemStack(Material.GUNPOWDER));
  }

  @Override
  public boolean canCraft(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    return feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.DWARF;
  }

  @Override
  public void onCraft(final Player player, final int amount) {

  }

}