package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeManager;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeModule;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
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
public class RecipeSelectionGUI implements InventoryProvider {

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }
    SmartInventory.builder().title("Deine Rezepte").size(5).provider(new RecipeSelectionGUI()).build()
        .open(player);
  }

  private final CustomRecipeManager customRecipeManager = FeroCore.getModule(CustomRecipeModule.class).getCustomRecipeManager();

  @Override
  public void init(final Player player, final InventoryContent content) {
    content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
    for (final CustomShapedRecipe recipe : this.customRecipeManager.getShapedRecipes()) {
      if (recipe.canPotentiallyCraft(player)) {
        content.add(this.getRecipeIcon(recipe));
      }
    }
  }

  private ClickableItem getRecipeIcon(final CustomShapedRecipe recipe) {
    final ItemStack result = recipe.getHandle().getResult();
    final ItemStack icon = new ItemBuilder(result).name("§eRezept: §r" + result.getItemMeta().getDisplayName()).build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RecipeViewGUI.open(player, recipe);
    });
  }

}
