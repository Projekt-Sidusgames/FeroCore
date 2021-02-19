package com.gestankbratwurst.ferocore.modules.racemodule.items.undead;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.modules.skillmodule.SkillShot;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
public class BlackWaveSkillshot extends SkillShot {

  public BlackWaveSkillshot(final Location startLoc) {
    super(startLoc, 0.3, true, 0.6);
  }

  @Override
  protected boolean onBlockImpact(final Block block, final BlockFace face) {
    this.hit(block.getLocation());
    return true;
  }

  @Override
  protected boolean onEntityImpact(final Entity entity, final Vector exactPosition) {
    this.hit(entity.getLocation());
    return true;
  }

  @Override
  protected void onRunout(final Location location) {
  }

  private void hit(final Location location) {
    final World world = location.getWorld();
    CustomSound.DARK_WAVE_HIT.playAt(location, SoundCategory.PLAYERS, 2.0F, 1F);
    world.playSound(location, Sound.ENTITY_PHANTOM_FLAP, 0.15F, 1F);
    world.spawnParticle(Particle.REDSTONE, location, 8, 2, 2, 2, 0.01, new DustOptions(Color.BLACK, 1F));
    world.spawnParticle(Particle.REDSTONE, location, 18, 2, 2, 2, 0.01, new DustOptions(Color.BLACK, 0.33F));
    world.spawnParticle(Particle.SMOKE_NORMAL, location, 10, 2, 2, 2, 0.01);
    location.getNearbyLivingEntities(2, 2, this::isValidTarget).forEach(living -> living.damage(5));
  }

  @Override
  protected void onFlyTick(final Location location) {
    final World world = location.getWorld();
    world.playSound(location, Sound.ENTITY_PHANTOM_FLAP, 0.25F, 0.1F);
    world.spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0.01, new DustOptions(Color.BLACK, 1F));
    world.spawnParticle(Particle.REDSTONE, location, 3, 0.1, 0.1, 0.1, 0.01, new DustOptions(Color.BLACK, 0.33F));
    world.spawnParticle(Particle.SMOKE_NORMAL, location, 1, 0, 0, 0, 0.01);
  }

  @Override
  protected int getMaxTicksAlive() {
    return 65;
  }

  @Override
  protected boolean isValidTarget(final Entity entity) {
    if (entity instanceof ArmorStand) {
      return false;
    }
    if (!(entity instanceof LivingEntity)) {
      return false;
    }
    if (entity instanceof Player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(entity.getUniqueId());
      return !feroPlayer.hasChosenRace() || feroPlayer.getRaceType() != RaceType.UNDEAD;
    }
    return true;
  }


}
