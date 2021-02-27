package com.gestankbratwurst.ferocore.modules.rolemodule.skills;

import com.gestankbratwurst.ferocore.modules.skillmodule.Debuff;
import com.gestankbratwurst.ferocore.util.common.UtilMobs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 25.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BleedingDebuff extends Debuff {

  private static final BlockData PARTICLE_DATA = Material.REDSTONE_BLOCK.createBlockData();

  public BleedingDebuff(final LivingEntity target) {
    super("DEBUFF_BLEEDING", target, 5, 20);
  }

  @Override
  public void applyTo(final LivingEntity entity) {
    UtilMobs.trueDamage(entity, DamageCause.ENTITY_ATTACK, 1.0);
    final World world = entity.getWorld();
    final Location spawnLoc = entity.getLocation().add(0, 0.5, 0);
    world.spawnParticle(Particle.BLOCK_DUST, spawnLoc, 8, 0.3, 0.3, 0.3, PARTICLE_DATA);
  }

  public static boolean isAffected(final LivingEntity livingEntity) {
    return Debuff.isAffected(livingEntity.getUniqueId(), "DEBUFF_BLEEDING");
  }

}