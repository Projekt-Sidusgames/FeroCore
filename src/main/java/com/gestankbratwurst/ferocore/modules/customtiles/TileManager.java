package com.gestankbratwurst.ferocore.modules.customtiles;

import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.container.CustomPersistentDataType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TileManager implements Iterable<CustomTile> {

  @Getter
  private final TileRegistry tileRegistry = new TileRegistry();
  private final Map<UUID, CustomTile> customTileMap = new HashMap<>();
  private final NamespacedKey customTileKey = NameSpaceFactory.provide("CUSTOM_TILE");
  private final NamespacedKey tileIdKey = NameSpaceFactory.provide("TILE_ID");
  private final NamespacedKey typeKey = NameSpaceFactory.provide("TILE_TYPE");
  private final NamespacedKey timeKey = NameSpaceFactory.provide("TILE_TIMESTAMP");

  public TileManager(final JavaPlugin plugin) {
    Bukkit.getPluginManager().registerEvents(new CustomTileListener(this), plugin);
    Bukkit.getScheduler().runTaskTimer(plugin, new TileRunnable(this), 1L, 1L);
  }

  public <T extends CustomTile> void register(final TileFactory<T> factory) {
    this.tileRegistry.register(factory);
  }

  public Optional<CustomTile> getCustomTile(final BlockState state) {
    if (!(state instanceof TileState)) {
      return Optional.empty();
    }
    final TileState holder = (TileState) state;
    final PersistentDataContainer container = holder.getPersistentDataContainer();
    if (container.isEmpty()) {
      return Optional.empty();
    }

    final UUID tileID = container.get(this.tileIdKey, CustomPersistentDataType.UUIDType);

    if (tileID == null) {
      return Optional.empty();
    }

    return Optional.of(this.customTileMap.get(tileID));
  }

  private void load(final BlockState state) {
    if (!(state instanceof TileState)) {
      return;
    }
    final TileState holder = (TileState) state;
    final PersistentDataContainer container = holder.getPersistentDataContainer();
    if (container.isEmpty()) {
      return;
    }

    final UUID tileID = container.get(this.tileIdKey, CustomPersistentDataType.UUIDType);

    if (tileID == null) {
      return;
    }

    final PersistentDataContainer tileContainer = container.get(this.customTileKey, PersistentDataType.TAG_CONTAINER);
    if (tileContainer == null) {
      System.out.println("TileContainer is null");
      return;
    }

    final String type = container.get(this.typeKey, PersistentDataType.STRING);
    final TileFactory<?> factory = this.tileRegistry.getFactory(type);
    if (factory == null) {
      System.out.println("Factory is null");
      return;
    }

    final Material material = factory.getBlockMaterial();
    if (state.getType() != material) {
      state.setType(material);
      state.update(true);
    }

    final long deltaTime = System.currentTimeMillis() - container.get(this.timeKey, PersistentDataType.LONG);

    final CustomTile tile = factory.createInstance(state);
    tile.load(tileContainer, deltaTime);
    this.customTileMap.put(tileID, tile);
    state.update(true);
  }

  private void save(final BlockState state) {
    if (!(state instanceof TileState)) {
      return;
    }
    final TileState holder = (TileState) state;
    final PersistentDataContainer container = holder.getPersistentDataContainer();
    if (container.isEmpty()) {
      return;
    }

    final UUID tileID = container.get(this.tileIdKey, CustomPersistentDataType.UUIDType);

    if (tileID == null) {
      return;
    }

    final CustomTile tile = this.customTileMap.remove(tileID);
    final PersistentDataContainer tileContainer = container.getAdapterContext().newPersistentDataContainer();
    final long currentTime = System.currentTimeMillis();
    tile.save(tileContainer, currentTime);

    container.set(this.customTileKey, PersistentDataType.TAG_CONTAINER, tileContainer);
    container.set(this.timeKey, PersistentDataType.LONG, currentTime);
    state.update(true);
  }

  public void create(final Block block, final String type) {
    final TileFactory<?> factory = this.tileRegistry.getFactory(type);
    if (factory == null) {
      return;
    }
    block.setType(factory.getBlockMaterial());

    final BlockState state = block.getState();
    final CustomTile tile = factory.createInstance(state);
    final UUID id = UUID.randomUUID();
    if (!(state instanceof TileState)) {
      return;
    }
    final TileState holder = (TileState) state;
    final PersistentDataContainer container = holder.getPersistentDataContainer();
    container.set(this.tileIdKey, CustomPersistentDataType.UUIDType, id);
    container.set(this.typeKey, PersistentDataType.STRING, type);
    this.customTileMap.put(id, tile);
    holder.update(true);
  }

  private void remove(final BlockState state) {
    if (!(state instanceof TileState)) {
      return;
    }
    final TileState holder = (TileState) state;
    final PersistentDataContainer container = holder.getPersistentDataContainer();
    if (container.isEmpty()) {
      return;
    }

    final UUID tileID = container.get(this.tileIdKey, CustomPersistentDataType.UUIDType);

    if (tileID == null) {
      return;
    }

    this.customTileMap.remove(tileID);
  }

  protected void handleEvent(final BlockBreakEvent event) {
    final BlockState state = event.getBlock().getState();
    final Optional<CustomTile> customTile = this.getCustomTile(state);
    customTile.ifPresent(tile -> {
      tile.onBreak(event);
      if (!event.isCancelled()) {
        this.remove(state);
      }
    });
  }

  protected void handleEvent(final BlockExplodeEvent event) {
    if (event.isCancelled()) {
      return;
    }
    event.blockList().stream().map(Block::getState).forEach(this::remove);
  }

  protected void handleEvent(final EntityExplodeEvent event) {
    if (event.isCancelled()) {
      return;
    }
    event.blockList().stream().map(Block::getState).forEach(this::remove);
  }

  protected void initChunk(final Chunk chunk) {
    for (final BlockState tileState : chunk.getTileEntities()) {
      this.load(tileState);
    }
  }

  protected void initWorld(final World world) {
    for (final Chunk chunk : world.getLoadedChunks()) {
      this.initChunk(chunk);
    }
  }

  protected void terminateChunk(final Chunk chunk) {
    for (final BlockState tileState : chunk.getTileEntities()) {
      this.save(tileState);
    }
  }

  protected void terminateWorld(final World world) {
    for (final Chunk chunk : world.getLoadedChunks()) {
      this.terminateChunk(chunk);
    }
  }

  @Override
  public Iterator<CustomTile> iterator() {
    return this.customTileMap.values().iterator();
  }

  @Override
  public void forEach(final Consumer<? super CustomTile> action) {
    this.customTileMap.values().forEach(action);
  }

  @Override
  public Spliterator<CustomTile> spliterator() {
    return this.customTileMap.values().spliterator();
  }
}