package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.UtilModule;
import com.gestankbratwurst.ferocore.util.actionbar.ActionBarBoard.Section;
import com.gestankbratwurst.ferocore.util.actionbar.ActionBarManager;
import com.gestankbratwurst.ferocore.util.actionbar.ActionLine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public enum PlayerActionPosition {

  LEFT("Links", Section.LEFT, Model.ACTION_BAR_ICON_LEFT.getItem()),
  MIDDLE("Mitte", Section.MIDDLE, Model.ACTION_BAR_ICON_MIDDLE.getItem()),
  RIGHT("Rechts", Section.RIGHT, Model.ACTION_BAR_ICON_RIGHT.getItem());

  @Getter
  private final String display;
  @Getter
  private final Section section;
  @Getter
  private final ItemStack baseIcon;

  public void apply(final Player player, final ActionLine actionLine) {
    final ActionBarManager actionBarManager = FeroCore.getModule(UtilModule.class).getActionBarManager();
    if (actionLine == null) {
      actionBarManager.getBoard(player).getSection(this.section).removeToken(this.display);
      return;
    }
    actionBarManager.getBoard(player).getSection(this.section).setTokenLayer(this.display, actionLine);
  }

}
