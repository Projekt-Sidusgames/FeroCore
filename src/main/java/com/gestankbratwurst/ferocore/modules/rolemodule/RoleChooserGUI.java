package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceMainGUI;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
public class RoleChooserGUI implements InventoryProvider {

  private static final Map<Integer, List<SlotPos>> DISTRIBUTED_POSITIONS = ImmutableMap.<Integer, List<SlotPos>>builder()
      .put(3, Arrays.asList(SlotPos.of(20), SlotPos.of(22), SlotPos.of(24)))
      .put(4, Arrays.asList(SlotPos.of(19), SlotPos.of(21), SlotPos.of(23), SlotPos.of(25)))
      .build();

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }
    SmartInventory.builder().title("Klasse wählen").provider(new RoleChooserGUI(feroPlayer.getRace())).size(5).build().open(player);
  }

  private final Race race;

  @Override
  public void init(final Player player, final InventoryContent content) {
    final List<RoleType> roleTypes = this.race.getChoosableRoles();
    final int len = roleTypes.size();
    final List<SlotPos> positions = DISTRIBUTED_POSITIONS.get(len);

    for (int i = 0; i < len; i++) {
      content.set(positions.get(i), this.getRoleIcon(roleTypes.get(i), player));
    }

    content.set(SlotPos.of(4, 0), RaceMainGUI.getBackToMainIcon());
  }

  private ClickableItem getRoleIcon(final RoleType roleType, final Player player) {
    final ItemStack icon = roleType.getIcon(player);
    return ClickableItem.empty(icon);
  }

}
