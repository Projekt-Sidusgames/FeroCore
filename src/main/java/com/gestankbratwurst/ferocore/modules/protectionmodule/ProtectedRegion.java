package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterable;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.function.LongConsumer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 30.06.2020
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ProtectedRegion implements Comparable<ProtectedRegion>, LongIterable {

  private static void setupEnemyRuleSet(final RuleSet ruleSet) {
    ruleSet.setState(ProtectionRule.PLAYER_INTERACT, RuleState.DENY);
    ruleSet.setState(ProtectionRule.BLOCK_PLACE, RuleState.DENY);
    ruleSet.setState(ProtectionRule.BLOCK_CHECK_BUILD, RuleState.DENY);
    ruleSet.setState(ProtectionRule.BREAK_BLOCK, RuleState.DENY);
    ruleSet.setState(ProtectionRule.SIGN_CHANGE, RuleState.DENY);
  }

  public ProtectedRegion(final BoundingBox regionBox, final RaceType owningRace, final int priority) {
    this.regionBox = regionBox;
    this.owningRace = owningRace;
    this.priority = priority;
    this.visitorRuleSet = new RuleSet(false);
    this.friendRuleSet = new RuleSet(false);
    this.enemyRuleSet = new RuleSet(false);
    this.globalRuleSet = new RuleSet(true);
    setupEnemyRuleSet(this.enemyRuleSet);
  }

  @Getter
  private final int priority;
  @Getter
  private final BoundingBox regionBox;
  @Getter
  private final RaceType owningRace;
  @Getter
  @Setter
  private String regionName = "UNBENANNT";
  @Getter
  private final RuleSet enemyRuleSet;
  @Getter
  private final RuleSet visitorRuleSet;
  @Getter
  private final RuleSet friendRuleSet;
  @Getter
  private final RuleSet globalRuleSet;

  public LongList getChunkKeysInRegion() {
    final LongList list = new LongArrayList();
    this.forEach((LongConsumer) list::add);
    return list;
  }

  @Override
  public int compareTo(final ProtectedRegion other) {
    return this.priority - other.priority;
  }

  @Override
  public LongIterator iterator() {
    return this.getChunkKeysInRegion().iterator();
  }

  @Override
  public void forEach(final LongConsumer action) {
    final Vector max = this.regionBox.getMax();
    final Vector min = this.regionBox.getMin();

    final int maxX = (int) Math.floor(max.getX()) >> 4;
    final int maxZ = (int) Math.floor(max.getZ()) >> 4;
    final int minX = (int) Math.floor(min.getX()) >> 4;
    final int minZ = (int) Math.floor(min.getZ()) >> 4;

    for (int x = minX; x <= maxX; x++) {
      for (int z = minZ; z <= maxZ; z++) {
        action.accept(Chunk.getChunkKey(x, z));
      }
    }
  }

}