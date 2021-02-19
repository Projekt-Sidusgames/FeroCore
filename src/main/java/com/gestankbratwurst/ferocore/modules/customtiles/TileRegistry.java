package com.gestankbratwurst.ferocore.modules.customtiles;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TileRegistry {

  private final Map<String, TileFactory<?>> factoryMap = new Object2ObjectOpenHashMap<>();

  public <T extends CustomTile> void register(final TileFactory<T> factory) {
    final String key = factory.getKey();
    this.factoryMap.put(key, factory);
  }

  protected TileFactory<?> getFactory(final String key) {
    return this.factoryMap.get(key);
  }

}
