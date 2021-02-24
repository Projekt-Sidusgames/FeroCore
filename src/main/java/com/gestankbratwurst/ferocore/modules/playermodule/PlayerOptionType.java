package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public enum PlayerOptionType {

  SHOW_DMG_HOLOGRAMS(new ItemBuilder(Material.IRON_SWORD).name("§eZeigen Schadens Hologramme").build());

  private final ItemStack baseItem;

  public ItemStack getIcon() {
    return this.baseItem.clone();
  }

}
