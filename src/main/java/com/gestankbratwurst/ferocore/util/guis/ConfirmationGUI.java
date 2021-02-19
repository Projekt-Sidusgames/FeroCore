package com.gestankbratwurst.ferocore.util.guis;

import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.AllArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 28.10.2020
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class ConfirmationGUI implements InventoryProvider {

  public static void open(final Player player, final BooleanConsumer consumer, final String title) {
    SmartInventory.builder().title(title).size(3).provider(new ConfirmationGUI(consumer)).build().open(player);
  }

  public static void open(final Player player, final BooleanConsumer consumer) {
    ConfirmationGUI.open(player, consumer, "Bist du dir sicher?");
  }

  private final BooleanConsumer consumer;

  @Override
  public void init(final Player player, final InventoryContent content) {

    content.set(SlotPos.of(1, 2), this.getAcceptItem());
    content.set(SlotPos.of(1, 6), this.getDenyItem());

  }

  private ClickableItem getAcceptItem() {
    final ItemBuilder builder = new ItemBuilder(Model.GREEN_CHECK.getItem()).name("§aJa");
    return ClickableItem.of(builder.build(), event -> {
      UtilPlayer.playUIClick((Player) event.getWhoClicked());
      this.consumer.accept(true);
    });
  }

  private ClickableItem getDenyItem() {
    final ItemBuilder builder = new ItemBuilder(Model.RED_X.getItem()).name("§cNein");
    return ClickableItem.of(builder.build(), event -> {
      UtilPlayer.playUIClick((Player) event.getWhoClicked());
      this.consumer.accept(false);
    });
  }

}