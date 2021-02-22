package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.RequiredArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RecipeViewGUI implements InventoryProvider {

  public static void open(final Player player, final CustomShapedRecipe recipe) {
    SmartInventory.builder().title("Rezept Ansicht").size(5).provider(new RecipeViewGUI(recipe)).build().open(player);
  }

  private final CustomShapedRecipe recipe;

  @Override
  public void init(final Player player, final InventoryContent content) {
    final ItemStack[] matrix = this.recipe.getCraftingMatrix();
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        content.set(SlotPos.of(y + 1, x + 2), ClickableItem.empty(matrix[x + y * 3]));
      }
    }
    content.set(SlotPos.of(2, 6), ClickableItem.empty(this.recipe.getHandle().getResult()));
    content.set(SlotPos.of(4, 0), this.getBackIcon());
    content.set(SlotPos.of(4, 4), ClickableItem.empty(new ItemBuilder(Model.RECIPE_VIEW_UI.getItem()).name(" ").build()));
  }

  private ClickableItem getBackIcon() {
    final ItemStack icon = new ItemBuilder(Model.DOUBLE_GRAY_ARROW_LEFT.getItem()).name("§eZurück").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceRecipeSelectionGUI.open(player);
    });
  }

}