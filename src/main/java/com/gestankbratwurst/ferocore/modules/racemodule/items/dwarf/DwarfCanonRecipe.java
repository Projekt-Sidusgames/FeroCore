package com.gestankbratwurst.ferocore.modules.racemodule.items.dwarf;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
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
public class DwarfCanonRecipe extends CustomShapedRecipe {

  public DwarfCanonRecipe() {
    super("dwarf_canon", DwarfCanonHandle.createCanon());
    this.setShape("iii", "n  ", "iii");
    this.setIngredient('i', new ItemStack(Material.IRON_BLOCK));
    this.setIngredient('n', new ItemStack(Material.NETHERITE_BLOCK));
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
