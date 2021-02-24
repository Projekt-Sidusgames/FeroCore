package com.gestankbratwurst.ferocore.modules.playermodule;

import lombok.Data;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 24.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
public class ChosenActionDisplay {

  private final PlayerActionBarType actionBarType;
  private final PlayerActionPosition actionPosition;

  public void apply(final Player player) {
    this.actionPosition.apply(player, this.actionBarType.produceLine(player));
  }

}
