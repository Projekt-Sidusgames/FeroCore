package com.gestankbratwurst.ferocore.modules.customrecipes;

import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import java.util.Arrays;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
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
public abstract class CustomShapedRecipe {

  public CustomShapedRecipe(final String name, final ItemStack result) {
    this.handle = new ShapedRecipe(NameSpaceFactory.provide(name), result);
  }

  @Getter
  private final ShapedRecipe handle;

  public void setMultiIngredient(final char key, final Material... materials) {
    final MaterialChoice choice = new MaterialChoice(Arrays.asList(materials));
    this.handle.setIngredient(key, choice);
  }

  public void setIngredient(final char key, final ItemStack item, final boolean exact) {
    final RecipeChoice choice;
    if (exact) {
      choice = new MaterialChoice(item.getType());
    } else {
      choice = new ExactChoice(item.asOne());
    }
    this.handle.setIngredient(key, choice);
  }

  public void setIngredient(final char key, final ItemStack item) {
    this.setIngredient(key, item, true);
  }

  public void setShape(final String... shape) {
    this.handle.shape(shape);
  }

  public abstract boolean canCraft(Player player);

  public abstract void onCraft(Player player, int amount);

  // TODO getShape
  // TODO getIngredients
}