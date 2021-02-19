package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.RequiredArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
public class ChatColorChooserGUI implements InventoryProvider {

  public static void open(final Player player) {
    SmartInventory.builder()
        .size(5)
        .title("Wähle eine Farbe")
        .provider(new ChatColorChooserGUI(FeroPlayer.of(player).getRace()))
        .build()
        .open(player);
  }

  private final Race race;

  @Override
  public void init(final Player player, final InventoryContent content) {

    content.set(SlotPos.of(1, 1), ClickableItem.of(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).name("§eGelb").build(), event -> {
      this.race.setNameChatColor(ChatColor.YELLOW);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    }));

    content.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).name("§9Blau").build(), event -> {
      this.race.setNameChatColor(ChatColor.BLUE);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    }));

    content.set(SlotPos.of(1, 3), ClickableItem.of(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).name("§6Orange").build(), event -> {
      this.race.setNameChatColor(ChatColor.GOLD);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    }));

    content
        .set(SlotPos.of(1, 4), ClickableItem.of(new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("§3Aqua").build(), event -> {
          this.race.setNameChatColor(ChatColor.DARK_AQUA);
          UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
          RaceLeaderGUI.open(player);
        }));

    content.set(SlotPos.of(1, 5), ClickableItem.of(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name("§aGrün").build(), event -> {
      this.race.setNameChatColor(ChatColor.GREEN);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    }));

    content.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.PINK_STAINED_GLASS_PANE).name("§dPink").build(), event -> {
      this.race.setNameChatColor(ChatColor.LIGHT_PURPLE);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    }));

    content.set(SlotPos.of(1, 7), ClickableItem.of(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("§7Grau").build(), event -> {
      this.race.setNameChatColor(ChatColor.GRAY);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    }));

    content.set(SlotPos.of(4, 0), RaceLeaderGUI.getLeadBackIcon());
  }
}
