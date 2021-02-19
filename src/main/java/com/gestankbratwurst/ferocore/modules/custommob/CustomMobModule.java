package com.gestankbratwurst.ferocore.modules.custommob;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.util.common.NMSUtils;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityTypes;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 15.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomMobModule implements BaseModule {

  private static final Map<String, EntityTypes<?>> ENTITY_REGISTRY = new HashMap<>();

  public static <T extends Entity> EntityTypes<T> getRegisteredType(final String key) {
    return (EntityTypes<T>) ENTITY_REGISTRY.get(key);
  }

  public static <T extends Entity> void registerCustomEntity(final String vanillaKey, final String customKey, final EntityTypes<T> baseType,
      final EntityTypes.b<T> construction, final int baseID) {
    ENTITY_REGISTRY.put(customKey, NMSUtils.register(baseType, vanillaKey, customKey, construction, baseID));
  }

  @Override
  public void enable(final FeroCore plugin) {
    
  }

  @Override
  public void disable(final FeroCore plugin) {

  }
}
