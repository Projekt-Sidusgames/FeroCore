package com.gestankbratwurst.ferocore.modules.customtiles;

import org.bukkit.Material;
import org.bukkit.block.BlockState;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface TileFactory<T extends CustomTile> {

  T createInstance(final BlockState state);

  Material getBlockMaterial();

  String getKey();

}