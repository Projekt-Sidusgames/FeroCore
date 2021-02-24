package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.Quest;
import lombok.RequiredArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.entity.Player;

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
public class RaceQuestGUI implements InventoryProvider {

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }
    SmartInventory.builder().size(5).title("Quests").provider(new RaceQuestGUI(feroPlayer.getRace())).build().open(player);
  }

  private final Race race;

  @Override
  public void init(final Player player, final InventoryContent content) {

    for (final Quest quest : this.race.getActiveQuests()) {
      content.add(ClickableItem.empty(quest.getIcon()));
    }

    content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
  }
}
