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
 * This file is part of FeroCore and was created at the 21.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RaceSkinChooserGUI implements InventoryProvider {

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }
    SmartInventory.builder().title("Wähle einen Skin").size(5).provider(new RaceSkinChooserGUI(feroPlayer.getRace())).build().open(player);
  }

  private final Race race;

  @Override
  public void init(final Player player, final InventoryContent content) {
    this.race.listChoosableSkins().stream().map(this::getSkinChooserIcon).forEach(content::add);
    content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
  }

  private ClickableItem getSkinChooserIcon(final Model skin) {
    final ItemStack icon = new ItemBuilder(skin.getHead())
        .name("§e" + skin.toString())
        .lore("")
        .lore("§fMomentan in der Rasse: §e" + this.race.getSkinPercentage(skin) + "%")
        .build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      final Model oldSkin = this.race.getSkinOf(player);
      this.race.applySkinChange(player.getUniqueId(), oldSkin, skin);
      UtilPlayer.playSound(player, Sound.ITEM_ARMOR_EQUIP_IRON, 0.75F, 0.5F);
      player.closeInventory();
    });
  }

}
