package com.gestankbratwurst.ferocore.modules.racemodule.items.human;

import com.gestankbratwurst.ferocore.modules.custommob.CustomMobModule;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 19.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class HolyCircleMarker extends EntityArmorStand {

  private final EntityTypes<? extends EntityArmorStand> type;

  public HolyCircleMarker(final EntityTypes<EntityArmorStand> type, final World world) {
    super(EntityTypes.ARMOR_STAND, world);
    this.type = type;
  }

  public HolyCircleMarker(final Location location) {
    super(EntityTypes.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle());
    this.setPosition(location.getX(), location.getY(), location.getZ());
    this.type = CustomMobModule.getRegisteredType("holy_circle");
    ((CraftWorld) location.getWorld()).getHandle().addEntity(this, SpawnReason.DEFAULT);
    this.setInvisible(true);
    this.setNoGravity(true);
    ((CraftArmorStand) this.getBukkitEntity()).setHelmet(Model.HOLY_CIRCLE.getItem());
  }

  public void rotateWallSign(final Block signBlock, final BlockFace face) {
    final BlockData data = signBlock.getBlockData();
    if (!(data instanceof Directional)) {
      return;
    }
    final Directional directional = (Directional) data;
    directional.setFacing(face);
    signBlock.setBlockData(data);
  }


  @Override
  public EntityTypes<? extends EntityArmorStand> getEntityType() {
    return this.type;
  }

  @Override
  public void tick() {
    super.tick();
    if (super.ticksLived % 65 == 0) {
      this.die();
    }
  }

  private void strikeLightning() {
    final Location location = this.getBukkitEntity().getLocation();
    location.getWorld().strikeLightningEffect(location);
    location.getNearbyLivingEntities(7.5, e -> e.getType() != EntityType.ARMOR_STAND).forEach(l -> l.damage(16));
  }

  @Override
  public void die(final DamageSource source) {
    this.die();
  }

  @Override
  public void die() {
    ((CraftArmorStand) this.getBukkitEntity()).setHelmet(null);
    this.strikeLightning();
    super.die();
  }

}
