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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 04.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RaceMemberListGUI implements InventoryProvider {

  public static void open(final Player player, final RaceType raceType) {
    final SmartInventory inv = SmartInventory.builder()
        .size(5)
        .provider(new RaceMemberListGUI(raceType.getRace(), raceType.getRace().isCrowned(player)))
        .title("Mitglieder der " + raceType.getRace().getDisplayName())
        .build();
    inv.open(player);
  }

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRace()) {
      open(player, feroPlayer.getRaceType());
    }
  }

  private final Race race;
  private final boolean viewerLeader;

  @Override
  public void init(final Player player, final InventoryContent content) {
    final boolean viewerIsMember = this.race.isMember(player.getUniqueId());
    this.race.forEachMemberAccount(fero -> content.add(this.getMemberIcon(fero)));
    if (viewerIsMember) {
      content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
    }
    content.set(SlotPos.of(4, 8), this.getDefaultRuleIcon());
  }

  private ClickableItem getDefaultRuleIcon() {
    final ItemStack icon = new ItemBuilder(Model.BROWN_BOOK.getItem())
        .name("§eDefault Regelwerk")
        .lore("")
        .lore("§7Diese Regeln hat jedes neue")
        .lore("§7Mitglied der Rasse und kann")
        .lore("§7als Einstellung für andere ver-")
        .lore("§7wendet werden.")
        .build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceRuleGUI.open(player, this.race.getDefaultRuleSet());
    });
  }

  private ClickableItem getMemberIcon(final FeroPlayer feroPlayer) {
    final ItemStack icon = new ItemBuilder(feroPlayer.getLastSeenHead()).name("§e" + feroPlayer.getLastSeenName()).build();
    return ClickableItem.of(icon, event -> {
      if (!this.viewerLeader) {
        return;
      }
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceRuleGUI.open(player, feroPlayer.getPlayerID());
    });
  }

}
