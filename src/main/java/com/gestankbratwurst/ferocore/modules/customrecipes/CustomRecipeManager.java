package com.gestankbratwurst.ferocore.modules.customrecipes;

import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.common.UtilInv;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ShapedRecipe;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomRecipeManager {

  private final Map<NamespacedKey, CustomShapedRecipe> shapedRecipeMap = new HashMap<>();

  public void registerShapedRecipe(final CustomShapedRecipe customShapedRecipe) {
    final ShapedRecipe recipe = customShapedRecipe.getHandle();
    this.shapedRecipeMap.put(recipe.getKey(), customShapedRecipe);
    Bukkit.addRecipe(recipe);
  }

  protected void handleShapedPrepare(final PrepareItemCraftEvent event, final ShapedRecipe recipe) {
    final CustomShapedRecipe customShapedRecipe = this.shapedRecipeMap.get(recipe.getKey());
    if (customShapedRecipe == null) {
      return;
    }
    final Player player = (Player) event.getView().getPlayer();
    if (!customShapedRecipe.canCraft(player)) {
      Msg.error(player, "Rezepte", "Das kannst du nicht craften.");
      event.getInventory().setResult(null);
    }
  }

  protected void handleShapedCraft(final CraftItemEvent event, final ShapedRecipe recipe) {
    final CustomShapedRecipe customShapedRecipe = this.shapedRecipeMap.get(recipe.getKey());
    if (customShapedRecipe == null) {
      return;
    }
    final Player player = (Player) event.getView().getPlayer();
    customShapedRecipe.onCraft(player, UtilInv.getAmountCrafted(event));
  }

}