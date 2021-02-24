package com.gestankbratwurst.ferocore.modules.playermodule;

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
 * This file is part of FeroCore and was created at the 24.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class PlayerActionChooserGUI implements InventoryProvider {

  public static void open(final Player player, final PlayerActionPosition actionPosition) {
    SmartInventory.builder()
        .title("Wähle Anzeige für " + actionPosition.getDisplay())
        .size(5)
        .provider(new PlayerActionChooserGUI(actionPosition))
        .build()
        .open(player);
  }

  private final PlayerActionPosition actionPosition;

  @Override
  public void init(final Player player, final InventoryContent content) {
    for (final PlayerActionBarType actionType : PlayerActionBarType.values()) {
      content.add(this.getIconOfType(actionType));
    }
    content.set(SlotPos.of(4, 0), this.getBackIcon());
  }

  private ClickableItem getIconOfType(final PlayerActionBarType type) {
    final ItemStack icon = type.getIcon();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      FeroPlayer.of(player).setChosenActionDisplay(this.actionPosition, type);
      this.reopen(player);
    });
  }

  private ClickableItem getBackIcon() {
    final ItemStack icon = new ItemBuilder(Model.DOUBLE_GRAY_ARROW_LEFT.getItem()).name("§cZurück").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      PlayerOptionGUI.open(player);
    });
  }

}
