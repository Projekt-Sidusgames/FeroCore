package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;
import java.util.UUID;
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
 * This file is part of FeroCore and was created at the 13.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RaceRuleGUI implements InventoryProvider {

  public static void open(final Player player, final UUID targetID) {

    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }

    final Race race = feroPlayer.getRace();
    if (!race.isMember(targetID)) {
      return;
    }

    final String lastSeenName = FeroPlayer.of(targetID).getLastSeenName();

    SmartInventory.builder().title("Rechte für " + lastSeenName)
        .size(5)
        .provider(new RaceRuleGUI(race.getRulesOf(targetID)))
        .build()
        .open(player);
  }

  public static void open(final Player player, final RaceRuleSet ruleSet) {
    Preconditions.checkArgument(ruleSet != null);
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }

    SmartInventory.builder().title("Default Rechte")
        .size(5)
        .provider(new RaceRuleGUI(ruleSet))
        .build()
        .open(player);
  }

  private final RaceRuleSet ruleSet;

  @Override
  public void init(final Player player, final InventoryContent content) {
    int x = 1;
    int y = 1;

    for (final Entry<RaceRuleType, RaceRuleState> entry : this.ruleSet) {
      final ClickableItem typeButton = this.getTypeButton(entry.getKey());
      final ClickableItem stateButton = this.getStateButton(entry.getKey(), entry.getValue());
      content.set(SlotPos.of(y, x), typeButton);
      content.set(SlotPos.of(y + 1, x), stateButton);
      x += 2;
      if (x == 7) {
        x = 1;
        y += 2;
      }
    }

    content.set(SlotPos.of(4, 0), this.getBackIcon());
  }

  private ClickableItem getBackIcon() {
    final ItemStack icon = new ItemBuilder(Model.DOUBLE_GRAY_ARROW_LEFT.getItem()).name("§eZurück").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceMemberListGUI.open(player);
    });
  }

  private ClickableItem getStateButton(final RaceRuleType type, final RaceRuleState currentState) {
    final ItemStack icon = currentState.getIcon();

    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      this.ruleSet.setState(type, currentState.next());
      this.reopen(player);
    });
  }


  private ClickableItem getTypeButton(final RaceRuleType type) {
    return ClickableItem.empty(type.getIcon());
  }

}
