package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public enum PlayerOptionValue {

  ENABLED(new ItemBuilder(Model.GREEN_CHECK.getItem()).name("§fMomentan: §aAn").lore("", "§7Klicke zum ändern").build()),
  DISABLE(new ItemBuilder(Model.RED_X.getItem()).name("§fMomentan: §cAus").lore("", "§7Klicke zum ändern").build());

  @Getter
  private final ItemStack icon;

}
