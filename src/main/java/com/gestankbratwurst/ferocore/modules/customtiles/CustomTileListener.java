package com.gestankbratwurst.ferocore.modules.customtiles;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class CustomTileListener implements Listener {

  private final TileManager tileManager;

  @EventHandler
  public void onBreak(final BlockBreakEvent event) {
    this.tileManager.handleEvent(event);
  }

  @EventHandler
  public void onExplosion(final BlockExplodeEvent event) {
    this.tileManager.handleEvent(event);
  }

  @EventHandler
  public void onExplosion(final EntityExplodeEvent event) {
    this.tileManager.handleEvent(event);
  }

  @EventHandler
  public void onChunkLoad(final ChunkLoadEvent event) {
    this.tileManager.initChunk(event.getChunk());
  }

  @EventHandler
  public void onChunkUnload(final ChunkUnloadEvent event) {
    this.tileManager.terminateChunk(event.getChunk());
  }

  @EventHandler
  public void onWorldLoad(final WorldLoadEvent event) {
    this.tileManager.initWorld(event.getWorld());
  }

  @EventHandler
  public void onWorldUnload(final WorldUnloadEvent event) {
    this.tileManager.terminateWorld(event.getWorld());
  }

}
