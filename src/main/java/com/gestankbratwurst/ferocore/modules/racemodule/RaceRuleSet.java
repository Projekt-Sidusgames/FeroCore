package com.gestankbratwurst.ferocore.modules.racemodule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 13.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RaceRuleSet implements Iterable<Entry<RaceRuleType, RaceRuleState>> {

  public RaceRuleSet() {
    this.ruleStates = new HashMap<>();
    for (final RaceRuleType type : RaceRuleType.values()) {
      this.ruleStates.put(type, RaceRuleState.DEFAULT);
    }
  }

  private final Map<RaceRuleType, RaceRuleState> ruleStates;

  public RaceRuleState getState(final RaceRuleType rule) {
    return this.ruleStates.get(rule);
  }

  public void setState(final RaceRuleType rule, final RaceRuleState state) {
    this.ruleStates.put(rule, state);
  }

  @Override
  @NotNull
  public Iterator<Entry<RaceRuleType, RaceRuleState>> iterator() {
    return this.ruleStates.entrySet().iterator();
  }
}