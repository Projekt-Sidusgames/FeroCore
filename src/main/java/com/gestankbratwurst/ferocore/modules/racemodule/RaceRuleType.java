package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.Arrays;
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
public enum RaceRuleType {

  BROADCAST("Ankündigungen", new String[]{"", "§7Erlaubt es dir Rassenglobale", "§7Ankündigungen zu machen"}, Model.HORN_ICON.getItem());

  private final String displayName;
  private final String[] desc;
  private final ItemStack icon;

  public ItemStack getIcon() {
    return new ItemBuilder(this.icon.clone()).name(this.displayName).lore(Arrays.asList(this.desc)).build();
  }

}
