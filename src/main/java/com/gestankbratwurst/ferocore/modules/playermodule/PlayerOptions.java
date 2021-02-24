package com.gestankbratwurst.ferocore.modules.playermodule;

import java.util.HashMap;
import java.util.Map;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PlayerOptions {

  private final Map<PlayerOptionType, PlayerOptionValue> optionMap;

  public PlayerOptions() {
    this.optionMap = new HashMap<>();
    for (final PlayerOptionType optionType : PlayerOptionType.values()) {
      this.optionMap.put(optionType, PlayerOptionValue.ENABLED);
    }
  }

  public PlayerOptionValue getSetting(final PlayerOptionType option) {
    return this.optionMap.get(option);
  }

  public boolean isEnabled(final PlayerOptionType option) {
    return this.optionMap.get(option) == PlayerOptionValue.ENABLED;
  }

  public PlayerOptionValue setSetting(final PlayerOptionType option, final PlayerOptionValue value) {
    return this.optionMap.put(option, value);
  }

  public PlayerOptionValue setSetting(final PlayerOptionType option, final boolean allow) {
    return this.optionMap.put(option, allow ? PlayerOptionValue.ENABLED : PlayerOptionValue.DISABLE);
  }

  public void toggleSetting(final PlayerOptionType option) {
    this.setSetting(option, this.getSetting(option) == PlayerOptionValue.DISABLE);
  }

}