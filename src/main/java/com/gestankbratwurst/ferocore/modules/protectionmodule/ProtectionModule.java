package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.modules.io.FeroIO;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.protectionmodule.RuleSet.RuleSetSerializer;
import com.gestankbratwurst.ferocore.modules.protectionmodule.json.ChunkDomainMapSerializer;
import com.gestankbratwurst.ferocore.modules.protectionmodule.json.RegionSetSerializer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.util.json.GsonProvider;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 30.06.2020
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ProtectionModule implements BaseModule, Listener {

  public ProtectionModule() {
    final FeroCore plugin = JavaPlugin.getPlugin(FeroCore.class);
    this.worlds = new Object2ObjectOpenHashMap<>();
    this.feroIO = plugin.getFeroIO();
    this.schematicManager = new SchematicManager();
  }

  private final FeroIO feroIO;
  private final Object2ObjectMap<UUID, WorldDomain> worlds;
  private final SchematicManager schematicManager;

  public WorldDomain getWorldDomain(final UUID worldID) {
    return this.worlds.get(worldID);
  }

  public ProtectedRegion createRegion(final Location corner1, final Location corner2, final RaceType owningRace, final int priority) {
    return this.createRegion(corner1.getBlock(), corner2.getBlock(), owningRace, priority);
  }

  protected ProtectedRegion createRegion(final BoundingBox box, final World world, final RaceType owningRace, final int priority) {
    final ProtectedRegion region = new ProtectedRegion(box, owningRace, priority);
    this.worlds.get(world.getUID()).addRegion(region);
    return region;
  }

  public ProtectedRegion createRegion(final Block corner1, final Block corner2, final RaceType owningRace, final int priority) {
    Preconditions.checkArgument(corner1.getWorld().equals(corner2.getWorld()));
    final BoundingBox box = BoundingBox.of(corner1, corner2);
    return this.createRegion(box, corner1.getWorld(), owningRace, priority);
  }

  public ProtectedRegion getHighestPriorityRegionAt(final Block block) {
    return this.worlds.get(block.getWorld().getUID()).getHighestPriorityRegionAt(block);
  }

  public RuleSet getPlayerRules(final Block block, final FeroPlayer feroPlayer) {
    return this.worlds.get(block.getWorld().getUID()).getPlayerRules(block, feroPlayer);
  }

  public RuleSet getEnvironmentRules(final Block block) {
    return this.worlds.get(block.getWorld().getUID()).getEnvironmentRules(block);
  }

  private void loadWorld(final UUID worldID) {
    final Optional<String> data = this.feroIO.loadWorldProtectionData(worldID);
    final WorldDomain domain = data.isEmpty() ? new WorldDomain(worldID) : GsonProvider.fromJson(data.get(), WorldDomain.class);
    domain.forceUpdate();
    this.worlds.put(worldID, domain);
  }

  private void unloadWorld(final UUID worldID) {
    final WorldDomain domain = this.worlds.get(worldID);
    if (domain != null) {
      this.feroIO.saveWorldProtectionData(worldID, GsonProvider.toJson(domain));
      this.worlds.remove(worldID);
    }
  }

  @EventHandler
  public void onLoad(final WorldLoadEvent event) {
    this.loadWorld(event.getWorld().getUID());
  }

  @EventHandler
  public void onUnload(final WorldUnloadEvent event) {
    this.unloadWorld(event.getWorld().getUID());
  }

  public void flushData() {
    for (final UUID worldID : new HashSet<>(this.worlds.keySet())) {
      this.unloadWorld(worldID);
    }
  }

  @Override
  public void enable(final FeroCore plugin) {
    GsonProvider.register(RuleSet.class, new RuleSetSerializer());
    GsonProvider.register(RegionSet.class, new RegionSetSerializer());
    GsonProvider.register(ChunkDomain.class, new ChunkDomainMapSerializer());
    FeroCore.registerListener(this);
    FeroCore.registerListener(new ProtectionListener(this));
    Bukkit.getScheduler().runTaskTimer(plugin, SchematicRunnable.getInstance(), 1, 1);
    this.schematicManager.load(this.feroIO.loadSchematicManager());
    for (final World world : Bukkit.getWorlds()) {
      this.loadWorld(world.getUID());
    }
    plugin.getPaperCommandManager().getCommandCompletions()
        .registerCompletion("Schematics", context -> this.schematicManager.getSchematicNames());
    plugin.getPaperCommandManager().registerCommand(new ProtectionCommand(this, this.schematicManager));
  }

  @Override
  public void disable(final FeroCore plugin) {
    this.feroIO.saveSchematicManager(this.schematicManager.save());
    this.flushData();
  }
}