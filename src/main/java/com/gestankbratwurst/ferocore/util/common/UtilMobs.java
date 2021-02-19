package com.gestankbratwurst.ferocore.util.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.v1_16_R3.BaseBlockPosition;
import net.minecraft.server.v1_16_R3.Chunk;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumMobSpawn;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.StructureBoundingBox;
import net.minecraft.server.v1_16_R3.StructureGenerator;
import net.minecraft.server.v1_16_R3.StructurePiece;
import net.minecraft.server.v1_16_R3.StructureStart;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 08.04.2020
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilMobs implements Listener {

  private static final String DOMESTICATION_TAG = "DOMESTIC";
  private static final Cache<Integer, Entity> ENTITY_UNLOAD_CACHE = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .build();

  public static Entity getEntity(final int id, final World world) {
    final net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftWorld) world).getHandle().getEntity(id);
    if (nmsEntity != null) {
      return nmsEntity.getBukkitEntity();
    }
    return UtilMobs.ENTITY_UNLOAD_CACHE.getIfPresent(id);
  }

  public static void init(final JavaPlugin plugin) {
    Bukkit.getPluginManager().registerEvents(new UtilMobs(plugin), plugin);
  }

  public static boolean isDomesticated(final LivingEntity entity) {
    return entity.getScoreboardTags().contains(UtilMobs.DOMESTICATION_TAG);
  }

  public static String serialize(final Entity entity) {
    final net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
    final NBTTagCompound compound = new NBTTagCompound();
    nmsEntity.a_(compound);
    return compound.toString();
  }

  public Optional<StructurePiece> isInsideStructure(final LivingEntity entity, final StructureGenerator<?> nmsType) {
    final Location entityLoc = entity.getLocation();
    final org.bukkit.Chunk entityChunk = entityLoc.getChunk();
    final WorldServer world = ((CraftWorld) entity.getWorld()).getHandle();
    final Chunk nmsEntityChunk = world.getChunkAt(entityChunk.getX(), entityChunk.getZ());
    final LongSet longset = nmsEntityChunk.b(nmsType);
    if (longset.size() == 0) {
      return Optional.empty();
    }

    for (final long possibleStructureStartKey : longset) {
      final int[] coords = UtilChunk.getChunkCoords(possibleStructureStartKey);
      final Chunk chunk = world.getChunkAt(coords[0], coords[1]);
      final Map<StructureGenerator<?>, StructureStart<?>> structureMap = chunk.h();
      final StructureStart<?> start = structureMap.get(nmsType);

      if (start == null) {
        continue;
      }

      final StructureBoundingBox boundingBox = start.c();
      final BaseBlockPosition baseEntityPosition = new BaseBlockPosition(entityLoc.getX(), entityLoc.getY(), entityLoc.getZ());
      final boolean isInside = boundingBox.b(baseEntityPosition);

      if (!isInside) {
        continue;
      }

      for (final StructurePiece piece : start.d()) {
        if (piece.g().b(baseEntityPosition)) {
          return Optional.of(piece);
        }
      }
    }

    return Optional.empty();
  }

  public static Entity deserialize(final String data, final Location location) throws CommandSyntaxException {
    final NBTTagCompound compound = MojangsonParser.parse(data);

    return UtilMobs.getEntityFromNBT(compound, location);
  }

  private static Entity getEntityFromNBT(final NBTTagCompound compound, final Location location) {
    final NBTTagCompound clone = compound.clone();

    final WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

    final net.minecraft.server.v1_16_R3.Entity entity = EntityTypes.a(clone, worldServer, (spawnedEntity) -> {

      spawnedEntity.dead = false;
      spawnedEntity.setUUID(UUID.randomUUID());
      spawnedEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getPitch());

      return !worldServer.addEntitySerialized(spawnedEntity) ? null : spawnedEntity;
    });
    if (entity != null) {
      if (entity instanceof EntityInsentient) {
        ((EntityInsentient) entity)
            .prepare(worldServer, worldServer.getDamageScaler(entity.getChunkCoordinates()), EnumMobSpawn.COMMAND, null, null);
      }
      return entity.getBukkitEntity();
    }
    return null;
  }

  private UtilMobs(final JavaPlugin plugin) {

  }

  private final Set<SpawnReason> domesticSpawnReasons = ImmutableSet.of(
      SpawnReason.BREEDING,
      SpawnReason.DISPENSE_EGG,
      SpawnReason.EGG);

  @EventHandler
  public void onSpawn(final CreatureSpawnEvent event) {
    if (this.domesticSpawnReasons.contains(event.getSpawnReason())) {
      event.getEntity().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
    }
  }

  @EventHandler
  public void onBreed(final EntityBreedEvent event) {
    if (event.getBreeder() == null) {
      return;
    }
    event.getMother().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
    event.getFather().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
  }

  @EventHandler
  public void onTame(final EntityTameEvent event) {
    event.getEntity().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
  }

  @EventHandler
  public void onUnload(final ChunkUnloadEvent event) {
    for (final Entity entity : event.getChunk().getEntities()) {
      UtilMobs.ENTITY_UNLOAD_CACHE.put(entity.getEntityId(), entity);
    }
  }

  @EventHandler
  public void onUnload(final WorldUnloadEvent event) {
    for (final Entity entity : event.getWorld().getEntities()) {
      UtilMobs.ENTITY_UNLOAD_CACHE.put(entity.getEntityId(), entity);
    }
  }

}
