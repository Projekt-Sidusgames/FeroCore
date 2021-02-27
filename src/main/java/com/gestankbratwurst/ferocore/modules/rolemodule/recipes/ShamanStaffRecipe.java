package com.gestankbratwurst.ferocore.modules.rolemodule.recipes;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleModule;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 24.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ShamanStaffRecipe extends CustomShapedRecipe {

  public ShamanStaffRecipe() {
    super("shaman_staff", createItem());
    super.setShape("  p", " s ", "s  ");
    super.setIngredient('s', new ItemStack(Material.STICK));
    super.setIngredient('p', ShamanStaffHeadRecipe.createItem());
  }

  @Override
  public boolean canCraft(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    return feroPlayer.hasChosenRole() && feroPlayer.getChosenRoleTye() == RoleType.SHAMAN;
  }

  @Override
  public void onCraft(final Player player, final int amount) {

  }

  public static ItemStack createItem() {
    return RoleModule.tagAsShamanStaff(new ItemBuilder(Model.SHAMAN_STAFF.getItem()).name("§fSchamanen Stab").build());
  }

}
