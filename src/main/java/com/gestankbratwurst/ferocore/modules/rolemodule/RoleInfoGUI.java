package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceMainGUI;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 27.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RoleInfoGUI implements InventoryProvider {

  public static void open(final Player player) {
    SmartInventory.builder().title("Klassen Menü").provider(new RoleInfoGUI()).size(5).build().open(player);
  }


  @Override
  public void init(final Player player, final InventoryContent content) {

    content.set(SlotPos.of(2, 4), ClickableItem.empty(FeroPlayer.of(player).getChosenRoleTye().getIcon(player)));

    content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
  }

}