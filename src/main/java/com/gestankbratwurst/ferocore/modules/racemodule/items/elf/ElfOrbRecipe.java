package com.gestankbratwurst.ferocore.modules.racemodule.items.elf;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 14.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ElfOrbRecipe extends CustomShapedRecipe {

  public ElfOrbRecipe() {
    super("elf_orb", ElfOrbHandle.createOrb(2));
    super.setShape(" p ", "pdp", " p ");
    super.setIngredient('d', new ItemStack(Material.DIAMOND), false);
    super.setIngredient('p', new ItemStack(Material.PRISMARINE_CRYSTALS), false);
  }

  @Override
  public boolean canCraft(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    return feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.ELF;
  }

  @Override
  public void onCraft(final Player player, final int amount) {

  }
}
