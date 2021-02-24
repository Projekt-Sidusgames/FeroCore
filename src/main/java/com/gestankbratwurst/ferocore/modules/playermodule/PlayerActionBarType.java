package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.util.actionbar.ActionLine;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 24.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public enum PlayerActionBarType {

  LEVEL_DISPLAY_PERCENT(new ItemBuilder(Material.PAPER).name("§eLevel Fortschritt prozentual").build()) {
    @Override
    public ActionLine produceLine(final Player player) {
      return new ActionLine(ActionLine.MID_PRIORITY, () -> FeroPlayer.of(player).getRoleLevelStringPercent());
    }
  },
  LEVEL_DISPLAY_NUMBERS(new ItemBuilder(Material.PAPER).name("§eLevel Fortschritt numerisch").build()) {
    @Override
    public ActionLine produceLine(final Player player) {
      return new ActionLine(ActionLine.MID_PRIORITY, () -> FeroPlayer.of(player).getRoleLevelStringNumber());
    }
  };

  @Getter
  private final ItemStack icon;

  public abstract ActionLine produceLine(Player player);

}
