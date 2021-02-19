package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.RequiredArgsConstructor;
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
public enum RaceRuleState {

  ALLOW("§aErlaubt", Model.GREEN_CHECK.getItem()),
  DENY("§cVerboten", Model.RED_X.getItem()),
  DEFAULT("§eVon Default geerbt", Model.DOUBLE_GRAY_ARROW_UP.getItem());

  private final String displayName;
  private final ItemStack icon;

  public ItemStack getIcon() {
    final ItemBuilder builder = new ItemBuilder(this.icon.clone());

    builder.name(this.displayName);
    builder.lore("", "§7Klicke zum wechseln.");

    return builder.build();
  }

  public RaceRuleState next() {
    final RaceRuleState[] states = RaceRuleState.values();
    final int pos = this.ordinal();
    final int nextPos = pos == states.length - 1 ? 0 : pos + 1;
    return states[nextPos];
  }

}
