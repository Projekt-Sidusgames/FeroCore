package com.gestankbratwurst.ferocore.modules.racemodule.items.undead;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.custommob.CustomMobModule;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.modules.skillmodule.SkillModule;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 17.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UndeadTotemEntity extends EntityArmorStand {

  private int maxTicksAlive;
  private final EntityTypes<? extends EntityArmorStand> type;
  private final SkillModule skillModule = FeroCore.getModule(SkillModule.class);

  public UndeadTotemEntity(final EntityTypes<EntityArmorStand> type, final World world) {
    super(EntityTypes.ARMOR_STAND, world);
    this.type = type;
  }

  public UndeadTotemEntity(final Location location, final int maxTicksAlive, final ItemStack headItem) {
    super(EntityTypes.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle());
    this.setPosition(location.getX(), location.getY(), location.getZ());
    this.type = CustomMobModule.getRegisteredType("temp_stand");
    ((CraftWorld) location.getWorld()).getHandle().addEntity(this, SpawnReason.DEFAULT);
    this.maxTicksAlive = maxTicksAlive;
    this.setInvisible(true);
    this.setNoGravity(true);
    ((CraftArmorStand) this.getBukkitEntity()).setHelmet(headItem);
  }

  @Override
  public EntityTypes<? extends EntityArmorStand> getEntityType() {
    return this.type;
  }

  @Override
  public void tick() {
    super.tick();
    if (super.ticksLived == this.maxTicksAlive) {
      this.die();
    }
    if (super.ticksLived % 60 == 0) {
      this.fireProjectile();
    }
  }

  private void fireProjectile() {
    double min = 256 * 256;
    LivingEntity minEnt = null;
    final Location location = this.getBukkitEntity().getLocation();
    for (final LivingEntity livingEntity : location.getNearbyLivingEntities(20, 8)) {
      if (livingEntity instanceof ArmorStand) {
        continue;
      }
      if (livingEntity instanceof Player) {
        final FeroPlayer feroPlayer = FeroPlayer.of(livingEntity.getUniqueId());
        if (feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.UNDEAD) {
          continue;
        }
      }
      final double distSq = livingEntity.getLocation().distanceSquared(location);
      if (distSq < min) {
        min = distSq;
        minEnt = livingEntity;
      }
    }
    if (minEnt == null) {
      return;
    }
    final Location startLoc = location.clone().add(0, 3, 0);
    final Vector direction = minEnt.getLocation().toVector().subtract(startLoc.toVector());
    startLoc.setDirection(direction);
    this.skillModule.addTickableSkill(new BlackWaveSkillshot(startLoc));
  }

  @Override
  public void die(final DamageSource source) {
    this.die();
  }

  @Override
  public void die() {
    ((CraftArmorStand) this.getBukkitEntity()).setHelmet(null);
    super.die();
  }

  @Override
  public void saveData(final NBTTagCompound compound) {
    super.saveData(compound);
    compound.setInt("maxTicksAlive", this.maxTicksAlive);

  }

  @Override
  public void loadData(final NBTTagCompound compound) {
    super.loadData(compound);
    this.maxTicksAlive = compound.getInt("maxTicksAlive");
  }

}
