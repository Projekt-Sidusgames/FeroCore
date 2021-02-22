package com.gestankbratwurst.ferocore.modules.customrecipes;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 07.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class CustomRecipeListener implements Listener {

  private final CustomRecipeManager recipeManager;

  @EventHandler
  public void onPrepare(final PrepareItemCraftEvent event) {
    final Recipe recipe = event.getRecipe();
    if (recipe instanceof ShapedRecipe) {
      this.recipeManager.handleShapedPrepare(event, (ShapedRecipe) recipe);
    }
  }

  @EventHandler
  public void onCraft(final CraftItemEvent event) {
    final Recipe recipe = event.getRecipe();
    if (recipe instanceof ShapedRecipe) {
      this.recipeManager.handleShapedCraft(event, (ShapedRecipe) recipe);
    }
  }

}
