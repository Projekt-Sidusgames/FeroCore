package com.gestankbratwurst.ferocore.modules.customtiles;

import lombok.RequiredArgsConstructor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class TileRunnable implements Runnable {

  private final TileManager tileManager;

  @Override
  public void run() {
    this.tileManager.forEach(CustomTile::tick);
  }

}
