package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map.Entry;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 01.07.2020
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RuleSet {

  public RuleSet(final boolean globalContext) {
    this.globalContext = globalContext;
    this.ruleStates = new EnumMap<>(ProtectionRule.class);
    for (final ProtectionRule rule : ProtectionRule.values()) {
      if (globalContext == rule.isGlobalContext()) {
        this.setState(rule, rule.getDefaultState());
      }
    }
  }

  @SerializedName("RuleStates")
  private final EnumMap<ProtectionRule, RuleState> ruleStates;
  @SerializedName("IsGlobalContext")
  private final boolean globalContext;

  public void setState(final ProtectionRule rule, final RuleState state) {
    if (rule.isGlobalContext() != this.globalContext) {
      throw new IllegalArgumentException("Tried to add rule with different context.");
    }
    this.ruleStates.put(rule, state);
  }

  public RuleState getState(final ProtectionRule rule) {
    if (rule.isGlobalContext() != this.globalContext) {
      throw new IllegalArgumentException("Tried to get rule with different context.");
    }
    return this.ruleStates.get(rule);
  }

  public static class RuleSetSerializer implements JsonDeserializer<RuleSet> {

    @Override
    public RuleSet deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
      final JsonObject baseObject = jsonElement.getAsJsonObject();
      final boolean global = baseObject.get("IsGlobalContext").getAsBoolean();
      final RuleSet ruleSet = new RuleSet(global);

      for (final Entry<String, JsonElement> entry : baseObject.get("RuleStates").getAsJsonObject().entrySet()) {
        ruleSet.ruleStates.put(ProtectionRule.valueOf(entry.getKey()), RuleState.valueOf(entry.getValue().getAsString()));
      }

      return ruleSet;
    }
  }

}