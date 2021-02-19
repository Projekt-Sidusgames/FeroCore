package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.FeroCore;
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
public class DiplomacyLeaderGUI implements InventoryProvider {

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRace()) {
      final RaceType race = feroPlayer.getRaceType();
      final RaceModule raceModule = FeroCore.getModule(RaceModule.class);
      SmartInventory.builder().size(5).title("Diplomatie").provider(new DiplomacyLeaderGUI(raceModule, race)).build().open(player);
    }
  }

  private final RaceModule raceModule;
  private final RaceType raceType;

  @Override
  public void init(final Player player, final InventoryContent content) {
    final RaceType[] types = RaceType.values();
    int index = 1;
    for (final RaceType currType : types) {
      if (currType != this.raceType) {
        final boolean atWar = this.raceType.getRace().isAtWarWith(currType);
        content.set(SlotPos.of(1, index), this.getDiplomacyIcon(currType, atWar));
        content.set(SlotPos.of(2, index), this.getWarIcon(currType, atWar));
        if (this.raceType.getRace().hasPeaceRequestFrom(currType)) {
          content.set(SlotPos.of(3, index), this.getPeaceRequestAcceptIcon(currType));
        }
        index += 2;
      }
    }

    content.set(SlotPos.of(4, 0), RaceLeaderGUI.getLeadBackIcon());
  }

  private ClickableItem getPeaceRequestAcceptIcon(final RaceType otherType) {
    final ItemStack icon = new ItemBuilder(Material.PAPER)
        .name("§aAnfrage: Frieden")
        .lore("")
        .lore("§7Es liegt ein Friedensangebot")
        .lore("§7vor. Linksklicke zum §aannehmen")
        .lore("§7und Rechtsklicke zum §cablehnen.")
        .build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      if (event.isRightClick()) {
        this.raceModule.declineRequest(this.raceType, otherType);
        UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
        this.reopen(player);
      } else if (event.isLeftClick()) {
        player.closeInventory();
        this.raceModule.declarePeace(this.raceType, otherType);
      }
    });
  }

  private ClickableItem getDiplomacyIcon(final RaceType otherType, final boolean atWar) {
    final Race otherRace = otherType.getRace();
    final String state = !atWar ? "§aFrieden §f" + Model.PEACE_ICON.getChar() : "§cKrieg §f" + Model.WAR_ICON.getChar();
    final ItemStack icon = new ItemBuilder(otherRace.getIcon().getItem())
        .name("§e" + otherRace.getDisplayName() + " §f" + otherRace.getIcon().getChar())
        .lore("")
        .lore("§7Status: " + state)
        .build();
    return ClickableItem.empty(icon);
  }

  private ClickableItem getWarIcon(final RaceType otherType, final boolean atWar) {
    ItemStack icon = new ItemBuilder(Model.WAR_ICON.getItem()).name("§cKrieg ausrufen!")
        .lore("")
        .lore("§7Ruft sofort den Krieg mit dieser")
        .lore("§7Rasse aus.")
        .lore("")
        .lore("§cVorsicht:")
        .lore("§7Kann nicht zurück genommen werden.")
        .build();
    if (atWar) {
      icon = new ItemBuilder(Model.PEACE_ICON.getItem())
          .name("§aFriedensangebot schicken")
          .lore("")
          .lore("§7Schickt ein Friedensangebot.")
          .lore("§7Muss nicht angenommen werden.")
          .build();
      if (otherType.getRace().hasPeaceRequestFrom(this.raceType)) {
        icon = new ItemBuilder(Material.PAPER)
            .name("§aFriedensangebot steht offen")
            .build();
      }
    }
    return ClickableItem.of(icon, event -> {
      if (!atWar) {
        this.raceModule.declareWar(this.raceType, otherType);
        event.getWhoClicked().closeInventory();
      } else {
        if (!otherType.getRace().hasPeaceRequestFrom(this.raceType)) {
          this.raceModule.sendPeaceRequest(this.raceType, otherType);
        }
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.UI_BUTTON_CLICK);
        this.reopen((Player) event.getWhoClicked());
      }
    });
  }

}
