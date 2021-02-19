package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.RequiredArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 05.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RaceLeaderGUI implements InventoryProvider {

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRace()) {
      final Race race = feroPlayer.getRace();
      final char raceIcon = race.getIcon().getChar();
      SmartInventory.builder()
          .size(5)
          .title("Anführermenü der " + race.getDisplayName() + " §f" + raceIcon)
          .provider(new RaceLeaderGUI(race))
          .build()
          .open(player);
    }
  }

  private final Race race;

  @Override
  public void init(final Player player, final InventoryContent content) {
    content.set(SlotPos.of(1, 1), this.getChatColorIcon());
    content.set(SlotPos.of(1, 3), this.getDiplomacyLeaderIcon());
    content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
  }

  private ClickableItem getChatColorIcon() {
    final ItemStack icon = new ItemBuilder(Material.WRITABLE_BOOK)
        .name("§ePräfix Farbe ändern")
        .lore("", "§7Momentan: " + this.race.getNameChatColor() + "⏹⏹⏹⏹⏹⏹⏹⏹⏹⏹")
        .build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      ChatColorChooserGUI.open(player);
    });
  }

  private ClickableItem getDiplomacyLeaderIcon() {
    final ItemStack icon = new ItemBuilder(Model.WAR_ICON.getItem()).name("§eDiplomatie verwalten").build();
    return ClickableItem.of(icon, event -> DiplomacyLeaderGUI.open((Player) event.getWhoClicked()));
  }

  public static ClickableItem getLeadBackIcon() {
    final ItemStack icon = new ItemBuilder(Model.DOUBLE_GRAY_ARROW_LEFT.getItem()).name("§cZurück").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    });
  }

}
