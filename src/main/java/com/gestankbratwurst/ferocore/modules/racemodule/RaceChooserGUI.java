package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import lombok.RequiredArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RaceChooserGUI implements InventoryProvider {

  public static void open(final Player player, final boolean canChoose) {
    final String title = canChoose ? "Wähle eine Rasse" : "Info über Rassen";
    SmartInventory.builder().size(3).title(title).provider(new RaceChooserGUI(canChoose)).build().open(player);
  }

  public static void open(final Player player) {
    open(player, true);
  }

  private final boolean canChoose;

  @Override
  public void init(final Player player, final InventoryContent content) {
    content.set(SlotPos.of(1, 0), this.getRaceChooserIcon(RaceType.HUMAN));
    content.set(SlotPos.of(1, 2), this.getRaceChooserIcon(RaceType.ELF));
    content.set(SlotPos.of(1, 4), this.getRaceChooserIcon(RaceType.DWARF));
    content.set(SlotPos.of(1, 6), this.getRaceChooserIcon(RaceType.UNDEAD));
    content.set(SlotPos.of(1, 8), this.getRaceChooserIcon(RaceType.ORC));
  }

  private ClickableItem getRaceChooserIcon(final RaceType raceType) {
    return ClickableItem.of(raceType.getRace().getInfoIcon(), event -> {
      if (!this.canChoose) {
        return;
      }
      final Player player = (Player) event.getWhoClicked();
      if (raceType.getRace().getMemberCount() - RaceType.getLowestMemberCount() >= 5) {
        Msg.send(player, "Rasse", "§cDiese Rasse ist übervölkert.");
        UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS);
        return;
      }
      FeroPlayer.of(player).addToRace(raceType);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      Msg.send(player, "Rasse", "Du hast " + Msg.elem(raceType.getRace().getDisplayName()) + " gewählt.");
      event.getWhoClicked().closeInventory();
    });
  }

}
