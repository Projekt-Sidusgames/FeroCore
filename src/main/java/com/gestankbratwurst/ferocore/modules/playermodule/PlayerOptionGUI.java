package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.modules.racemodule.RaceMainGUI;
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
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class PlayerOptionGUI implements InventoryProvider {

  public static void open(final Player player) {
    SmartInventory.builder()
        .size(5)
        .title("Optionen")
        .provider(new PlayerOptionGUI(FeroPlayer.of(player).getPlayerOptions()))
        .build()
        .open(player);
  }

  private final PlayerOptions playerOptions;

  @Override
  public void init(final Player player, final InventoryContent content) {
    final PlayerOptionType[] playerOptionTypes = PlayerOptionType.values();
    for (int i = 0; i < playerOptionTypes.length; i++) {
      final int x = i % 8;
      final int y = (i / 8) * 2;
      content.set(SlotPos.of(y, x), this.getInfoIcon(playerOptionTypes[i]));
      content.set(SlotPos.of(y + 1, x), this.getChangerIcon(playerOptionTypes[i]));
    }
    if (FeroPlayer.of(player).hasChosenRace()) {
      content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
    }
    content.set(SlotPos.of(4, 3), this.getActionDisplayIcon(PlayerActionPosition.LEFT));
    content.set(SlotPos.of(4, 4), this.getActionDisplayIcon(PlayerActionPosition.MIDDLE));
    content.set(SlotPos.of(4, 5), this.getActionDisplayIcon(PlayerActionPosition.RIGHT));
  }

  private ClickableItem getInfoIcon(final PlayerOptionType optionType) {
    return ClickableItem.empty(optionType.getIcon());
  }

  private ClickableItem getActionDisplayIcon(final PlayerActionPosition position) {
    final ItemStack icon = new ItemBuilder(position.getBaseIcon().clone()).name("§eAktions Leiste: §f" + position.getDisplay()).build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      PlayerActionChooserGUI.open(player, position);
    });
  }

  private ClickableItem getChangerIcon(final PlayerOptionType optionType) {
    final PlayerOptionValue value = this.playerOptions.getSetting(optionType);
    return ClickableItem.of(value.getIcon(), event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      this.playerOptions.toggleSetting(optionType);
      this.reopen(player);
    });
  }

}