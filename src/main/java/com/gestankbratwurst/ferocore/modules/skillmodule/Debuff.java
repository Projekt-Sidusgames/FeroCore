package com.gestankbratwurst.ferocore.modules.skillmodule;

import com.gestankbratwurst.ferocore.FeroCore;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 25.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class Debuff implements TickableSkill {

  public static Set<Debuff> getDebuffsOf(final UUID targetID) {
    return new HashSet<>(ENTITY_DEBUFF_CACHE.get(targetID));
  }

  protected static boolean isAffected(final UUID entityID, final String debuffKey) {
    final Map<UUID, Debuff> debuffMap = DEBUFF_CACHE.get(debuffKey);
    return debuffMap != null && debuffMap.containsKey(entityID);
  }

  private static final Map<String, Map<UUID, Debuff>> DEBUFF_CACHE = new Object2ObjectOpenHashMap<>();
  private static final Map<UUID, Set<Debuff>> ENTITY_DEBUFF_CACHE = new Object2ObjectOpenHashMap<>();

  private final String debuffKey;
  private final LivingEntity target;
  private final int tickAmount;
  private final int tickRate;
  private int ticks = 0;
  private int tickCounter = 0;
  private boolean done = false;
  private boolean temp;

  protected Debuff(final String debuffKey, final LivingEntity target, final int tickAmount, final int tickRate) {
    final Map<UUID, Debuff> debuffMap = DEBUFF_CACHE.computeIfAbsent(debuffKey, (key) -> new HashMap<>());
    final UUID targetID = target.getUniqueId();
    final Debuff current = debuffMap.get(targetID);
    if (current == null) {
      debuffMap.put(targetID, this);
      ENTITY_DEBUFF_CACHE.computeIfAbsent(targetID, (key) -> new HashSet<>()).add(this);
      this.temp = false;
    } else {
      this.temp = true;
      this.done = true;
      current.ticks = 0;
      current.tickCounter = 0;
    }
    this.debuffKey = debuffKey;
    this.target = target;
    this.tickAmount = tickAmount;
    this.tickRate = tickRate;
  }

  public void start() {
    FeroCore.getModule(SkillModule.class).addTickableSkill(this);
  }

  public void stop() {
    this.cleanAndQuit();
    this.temp = true;
  }

  private void cleanAndQuit() {
    DEBUFF_CACHE.get(this.debuffKey).remove(this.target.getUniqueId());
    final Set<Debuff> debuffSet = ENTITY_DEBUFF_CACHE.get(this.target.getUniqueId());
    debuffSet.remove(this);
    if (debuffSet.isEmpty()) {
      ENTITY_DEBUFF_CACHE.remove(this.target.getUniqueId());
    }
    this.done = true;
  }

  @Override
  public void tick() {
    if (this.temp) {
      return;
    }
    if (this.target.isDead()) {
      this.cleanAndQuit();
      return;
    }
    if (++this.ticks % this.tickRate == 0) {
      this.applyTo(this.target);
      if (++this.tickCounter == this.tickAmount) {
        this.cleanAndQuit();
      }
    }
  }

  @Override
  public boolean isDone() {
    return this.done;
  }

  public abstract void applyTo(LivingEntity entity);

}
