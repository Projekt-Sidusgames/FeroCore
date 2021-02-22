package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.google.gson.annotations.SerializedName;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.LongConsumer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 01.07.2020
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class WorldDomain {

  protected WorldDomain() {
    this(null);
  }

  public WorldDomain(final UUID worldID) {
    this.worldID = worldID;
    this.playerRules = new RuleSet(false);
    this.globalRules = new RuleSet(true);
    this.chunks = new ChunkDomainMap();
  }

  @Getter
  @Setter
  private int worldPlayerRulePriority = 0;
  @Getter
  @Setter
  private int worldGlobalRulePriority = 0;
  @SerializedName("WorldUUID")
  private final UUID worldID;
  private final RuleSet playerRules;
  private final RuleSet globalRules;
  private final RegionSet regionSet = new RegionSet();
  private final transient ChunkDomainMap chunks;

  public ChunkDomain getChunkDomain(final long chunkKey) {
    return this.chunks.get(chunkKey);
  }

  public ProtectedRegion getHighestPriorityRegionAt(final Block block) {
    final ChunkDomain chunkDomain = this.getChunkDomain(block.getChunk().getChunkKey());
    return chunkDomain == null ? null : chunkDomain.getHighestPriorityRegion(block.getLocation().toVector());
  }

  public void setWorldPlayerRule(final ProtectionRule rule, final RuleState state) {
    this.playerRules.setState(rule, state);
  }

  public void setWorldGlobalRule(final ProtectionRule rule, final RuleState state) {
    this.globalRules.setState(rule, state);
  }

  public RuleSet getPlayerRules(final Block block, final FeroPlayer feroPlayer) {
    final ProtectedRegion region = this.getHighestPriorityRegionAt(block);
    if (region == null || region.getPriority() < this.worldPlayerRulePriority) {
      return this.playerRules;
    }
    if (region.getOwningRace() == null || !feroPlayer.hasChosenRace() || feroPlayer.getRace().isAtWarWith(region.getOwningRace())) {
      return region.getEnemyRuleSet();
    }
    return region.getFriendRuleSet();
  }

  public RuleSet getEnvironmentRules(final Block block) {
    final ProtectedRegion region = this.getHighestPriorityRegionAt(block);
    if (region == null || region.getPriority() < this.worldGlobalRulePriority) {
      return this.globalRules;
    }
    return region.getGlobalRuleSet();
  }

  public void addRegion(final ProtectedRegion protectedRegion) {
    this.regionSet.add(protectedRegion);
    protectedRegion.forEach((LongConsumer) chunkKey -> {
      ChunkDomain chunkDomain = this.chunks.get(chunkKey);
      if (chunkDomain == null) {
        chunkDomain = new ChunkDomain();
      }
      chunkDomain.addRegion(protectedRegion);
      this.chunks.put(chunkKey, chunkDomain);
    });
  }

  protected void forceUpdate() {
    for (final ProtectedRegion region : new HashSet<>(this.regionSet)) {
      this.addRegion(region);
    }
  }

}