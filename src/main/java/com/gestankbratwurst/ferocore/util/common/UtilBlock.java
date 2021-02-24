package com.gestankbratwurst.ferocore.util.common;

import com.gestankbratwurst.ferocore.util.common.sub.BlockPosition;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 26.03.2020
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilBlock implements Listener {

  private static UtilBlock listenerInstance;

  private static final Map<World, Long2ObjectOpenHashMap<Set<BlockPosition>>> PLAYER_PLACED_BLOCKS = new Object2ObjectOpenHashMap<>();

  public static void terminate(final JavaPlugin plugin) {
    for (final World world : Bukkit.getWorlds()) {
      for (final Chunk chunk : world.getLoadedChunks()) {
        UtilBlock.listenerInstance.terminateChunk(chunk);
      }
      UtilBlock.listenerInstance.terminateWorld(world);
    }
  }

  public static void init(final JavaPlugin plugin) {
    UtilBlock.listenerInstance = new UtilBlock(plugin);
    Bukkit.getPluginManager().registerEvents(UtilBlock.listenerInstance, plugin);
  }

  public static boolean isPlayerPlaced(final Block block) {
    final Long2ObjectMap<Set<BlockPosition>> chunkMap = UtilBlock.PLAYER_PLACED_BLOCKS.get(block.getWorld());
    if (chunkMap == null) {
      return false;
    }
    final Set<BlockPosition> positionSet = chunkMap.get(UtilChunk.getChunkKey(block.getLocation()));
    if (positionSet == null) {
      return false;
    }
    return positionSet.contains(new BlockPosition(block));
  }

  public static boolean isPlayerPlaced(final Location location) {
    return UtilBlock.isPlayerPlaced(location.getBlock());
  }

  private final JavaPlugin plugin;
  private final File dataFolder;
  private final ConcurrentHashMap<World, File> worldFolderMap;
  private final TaskManager taskManager;

  private UtilBlock(final JavaPlugin plugin) {
    this.plugin = plugin;
    this.dataFolder = new File(plugin.getDataFolder() + File.separator + "playerblockcache");
    this.worldFolderMap = new ConcurrentHashMap<>();
    this.taskManager = TaskManager.getInstance();
    if (!this.dataFolder.exists()) {
      this.dataFolder.mkdirs();
    }
    for (final World world : Bukkit.getWorlds()) {
      this.initWorld(world);
      for (final Chunk chunk : world.getLoadedChunks()) {
        this.initChunk(chunk);
      }
    }
  }


  private void initWorld(final World world) {
    final Long2ObjectOpenHashMap<Set<BlockPosition>> chunkMap = new Long2ObjectOpenHashMap<>();
    UtilBlock.PLAYER_PLACED_BLOCKS.put(world, chunkMap);
    CompletableFuture
        .runAsync(() -> this.createAndLoadFile(world))
        .thenRun(() -> this.taskManager.runBukkitSync(() -> this.initChunksInWorld(world)));
  }

  private void createAndLoadFile(final World world) {
    final File folder = new File(this.dataFolder + File.separator + world.getUID().toString());
    if (!folder.exists()) {
      folder.mkdirs();
    }
    this.worldFolderMap.put(world, folder);
  }

  private void initChunksInWorld(final World world) {
    for (final Chunk chunk : world.getLoadedChunks()) {
      this.initChunk(chunk);
    }
  }

  private void terminateWorld(final World world) {
    for (final Chunk chunk : world.getLoadedChunks()) {
      this.terminateChunk(chunk);
    }
    UtilBlock.PLAYER_PLACED_BLOCKS.remove(world);
  }

  private void initChunk(final Chunk chunk) {
    final World world = chunk.getWorld();
    final long chunkKey = UtilChunk.getChunkKey(chunk);
    if (UtilBlock.PLAYER_PLACED_BLOCKS.get(world).containsKey(chunkKey)) {
      return;
    }
    CompletableFuture.runAsync(() -> {
      final File file = new File(this.worldFolderMap.get(world), chunkKey + ".cpbd");
      final ByteBuffer buffer = this.readByteBufferFromFile(file);
      final Set<BlockPosition> positions = this.readPositionsFromByteBuffer(buffer);
      this.taskManager.runBukkitSync(() -> UtilBlock.PLAYER_PLACED_BLOCKS.get(world).put(chunkKey, positions));
    });
  }

  private ByteBuffer readByteBufferFromFile(final File file) {
    ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
    buffer.putInt(0);
    if (file.exists()) {
      try (final FileInputStream inputStream = new FileInputStream(file)) {
        buffer = ByteBuffer.wrap(inputStream.readAllBytes());
      } catch (final IOException ignored) {
      }
    }
    return buffer;
  }

  private Set<BlockPosition> readPositionsFromByteBuffer(final ByteBuffer byteBuffer) {
    byteBuffer.rewind();
    final Set<BlockPosition> positionSet = new ObjectOpenHashSet<>();
    if (byteBuffer.hasRemaining()) {
      final int length = byteBuffer.getInt();
      for (int index = 0; index < length; index++) {
        positionSet.add(new BlockPosition(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt()));
      }
    }
    return positionSet;
  }

  private void terminateChunk(final Chunk chunk) {
    final World world = chunk.getWorld();
    final long chunkKey = UtilChunk.getChunkKey(chunk);
    final Set<BlockPosition> positions = UtilBlock.PLAYER_PLACED_BLOCKS.get(world).remove(chunkKey);

    if (positions == null) {
      return;
    }

    CompletableFuture.runAsync(() -> {
      final File file = new File(this.worldFolderMap.get(world), chunkKey + ".cpbd");
      this.savePositions(file, positions);
    });
  }

  private void savePositions(final File file, final Set<BlockPosition> positions) {
    final int size = positions.size();
    if (size == 0) {
      if (file.exists()) {
        file.delete();
      }
      return;
    }
    try {
      final FileOutputStream fos = new FileOutputStream(file);
      final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * 3 * size + Integer.BYTES);
      buffer.putInt(size);
      for (final BlockPosition position : positions) {
        buffer.putInt(position.x);
        buffer.putInt(position.y);
        buffer.putInt(position.z);
      }
      fos.write(buffer.array());
      fos.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void initBlock(final Block block) {
    final Chunk chunk = block.getChunk();
    final long chunkKey = UtilChunk.getChunkKey(chunk);
    final Long2ObjectMap<Set<BlockPosition>> chunkMap = UtilBlock.PLAYER_PLACED_BLOCKS.get(block.getWorld());
    if (chunkMap == null) {
      this.plugin.getLogger().severe("World is not initialized for Block saves!");
      return;
    }
    final Set<BlockPosition> positionSet = chunkMap.get(chunkKey);
    final BlockPosition position = new BlockPosition(block);
    if (positionSet == null) {
      this.plugin.getLogger().warning("Player Block Tracker compromised!");
      this.plugin.getLogger().warning("@ " + position.toString());
      return;
    }
    positionSet.add(position);
  }

  private void terminateBlock(final Block block) {
    final Long2ObjectMap<Set<BlockPosition>> chunkMap = UtilBlock.PLAYER_PLACED_BLOCKS.get(block.getWorld());
    if (chunkMap == null) {
      return;
    }
    final long chunkKey = UtilChunk.getChunkKey(block.getLocation());
    final Set<BlockPosition> positionSet = chunkMap.get(chunkKey);
    if (positionSet == null) {
      return;
    }
    TaskManager.getInstance().runBukkitSync(() -> positionSet.remove(new BlockPosition(block)));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onWorldLoad(final WorldLoadEvent event) {
    this.initWorld(event.getWorld());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldUnload(final WorldUnloadEvent event) {
    this.terminateWorld(event.getWorld());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChunkLoad(final ChunkLoadEvent event) {
    this.initChunk(event.getChunk());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChunkUnload(final ChunkUnloadEvent event) {
    this.terminateChunk(event.getChunk());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPistonMove(final BlockPistonExtendEvent event) {
    if (event.isCancelled()) {
      return;
    }
    final List<Block> blocks = event.getBlocks();
    if (blocks.size() > 0) {
      final BlockFace dir = event.getDirection();
      this.terminateBlock(blocks.get(0));
      this.initBlock(blocks.get(blocks.size() - 1).getRelative(dir));
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPistonMove(final BlockPistonRetractEvent event) {
    if (event.isCancelled()) {
      return;
    }
    final List<Block> blocks = event.getBlocks();
    if (blocks.size() > 0) {
      final BlockFace dir = event.getDirection();
      this.terminateBlock(blocks.get(0));
      this.initBlock(blocks.get(blocks.size() - 1).getRelative(dir));
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockPlaced(final BlockPlaceEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
      return;
    }
    this.initBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBreak(final BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBurn(final BlockBurnEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onExplode(final BlockExplodeEvent event) {
    if (event.isCancelled()) {
      return;
    }
    for (final Block exploded : event.blockList()) {
      this.terminateBlock(exploded);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFade(final BlockFadeEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFertilize(final BlockFertilizeEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
    for (final BlockState state : event.getBlocks()) {
      this.terminateBlock(state.getBlock());
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onForm(final BlockFormEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onGrow(final BlockGrowEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onSpread(final BlockSpreadEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEForm(final EntityBlockFormEvent event) {
    if (event.isCancelled()) {
      return;
    }
    this.terminateBlock(event.getBlock());
  }

}