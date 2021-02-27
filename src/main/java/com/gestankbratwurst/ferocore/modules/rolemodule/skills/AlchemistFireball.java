package com.gestankbratwurst.ferocore.modules.rolemodule.skills;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.skillmodule.SkillShot;
import com.gestankbratwurst.ferocore.util.common.UtilMobs;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 26.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class AlchemistFireball extends SkillShot {

  public AlchemistFireball(final Location startLoc, final FeroPlayer caster) {
    super(startLoc, 0.5D, false, 0.66);
    this.feroCaster = caster;
  }

  private final FeroPlayer feroCaster;

  @Override
  public void start() {
    this.feroCaster.getOnlinePlayer().ifPresent(pl -> {
      final Location location = pl.getEyeLocation();
      location.getWorld().playSound(location, Sound.ENTITY_BLAZE_SHOOT, 0.6F, 0.66F);
    });
    super.start();
  }

  @Override
  protected boolean onBlockImpact(final Block block, final BlockFace face) {
    final Location location = block.getLocation();
    location.getWorld().playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1F, 1F);
    return true;
  }

  @Override
  protected boolean onEntityImpact(final Entity entity, final Vector exactPosition) {
    final LivingEntity livingEntity = (LivingEntity) entity;
    final Location location = entity.getLocation();
    location.getWorld().spawnParticle(Particle.FLAME, location, 12, 0.5, 0.5, 0.5, 0.1);
    location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.2F, 0.2F);
    UtilMobs.trueDamage(livingEntity, DamageCause.MAGIC, 8);
    return true;
  }

  @Override
  protected void onRunout(final Location location) {
    location.getWorld().playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1F, 1F);
  }

  @Override
  protected void onFlyTick(final Location location) {
    location.getWorld().spawnParticle(Particle.FLAME, location, 2, 0.02, 0.02, 0.02, 0.0125);
    location.getWorld().playSound(location, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 0.4F, 1.75F);
  }

  @Override
  protected int getMaxTicksAlive() {
    return 50;
  }

  @Override
  protected boolean isValidTarget(final Entity entity) {
    final UUID hitId = entity.getUniqueId();
    if (hitId.equals(this.feroCaster.getPlayerID())) {
      return false;
    }
    if (!(entity instanceof LivingEntity)) {
      return false;
    }
    if (!(entity instanceof Player)) {
      return true;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(hitId);
    return !feroPlayer.hasChosenRace() || feroPlayer.getRaceType() != this.feroCaster.getRaceType();
  }
}
