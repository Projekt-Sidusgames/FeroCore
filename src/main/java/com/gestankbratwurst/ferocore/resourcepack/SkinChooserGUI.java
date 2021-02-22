package com.gestankbratwurst.ferocore.resourcepack;

import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
 * This file is part of FeroCore and was created at the 21.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class SkinChooserGUI implements InventoryProvider {

  private static final List<ClickableItem> CHOOSER_ICONS = Arrays.stream(Model.values())
      .filter(Model::isPlayerSkinModel)
      .map(model -> ClickableItem.of(new ItemBuilder(model.getHead()).name("§e" + model.toString()).build(), event -> {
        Player player = (Player) event.getWhoClicked();
        model.applySkinTo(player);
        UtilPlayer.playSound(player, Sound.ITEM_ARMOR_EQUIP_IRON, 0.75F, 0.5F);
        player.closeInventory();
      })).collect(Collectors.toList());

  public static void open(final Player player) {
    open(player, 0);
  }

  private static void open(final Player player, final int page) {
    SmartInventory.builder().title("Wähle einen Skin [" + (page + 1) + "]").size(5).provider(new SkinChooserGUI(page)).build().open(player);
  }

  private final int page;
  private final int max = CHOOSER_ICONS.size();

  @Override
  public void init(final Player player, final InventoryContent content) {
    if (this.page > 0) {
      content.set(SlotPos.of(4, 0), this.getBackIcon());
    }
    boolean next = true;
    for (int i = this.page * 36; i < (this.page + 1) * 36; i++) {
      if (i == this.max) {
        next = false;
        break;
      }
      content.add(CHOOSER_ICONS.get(i));
      if (i + 1 == this.max) {
        next = false;
        break;
      }
    }
    if (next) {
      content.set(SlotPos.of(4, 8), this.getForwardIcon());
    }
  }

  private ClickableItem getBackIcon() {
    final ItemStack icon = new ItemBuilder(Model.DOUBLE_GRAY_ARROW_LEFT.getItem()).name("§eSeite " + this.page).build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      open(player, this.page - 1);
    });
  }

  private ClickableItem getForwardIcon() {
    final ItemStack icon = new ItemBuilder(Model.DOUBLE_GRAY_ARROW_RIGHT.getItem()).name("§eSeite " + (this.page + 2)).build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      open(player, this.page + 1);
    });
  }

}
