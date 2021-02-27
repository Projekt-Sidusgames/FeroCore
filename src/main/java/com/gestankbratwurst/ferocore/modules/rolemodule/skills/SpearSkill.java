package com.gestankbratwurst.ferocore.modules.rolemodule.skills;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.util.common.UtilMobs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 27.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SpearSkill {

  private static Location baseLoc;

  public static void cast(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRole() || !feroPlayer.hasChosenRace()) {
      return;
    }
    final RaceType raceType = feroPlayer.getRaceType();
    final World world = player.getWorld();
    baseLoc = player.getLocation().add(0, 0.5, 0);
    world.playSound(baseLoc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 2F);
    world.playSound(baseLoc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.2F);
    // world.spawnParticle(Particle.SWEEP_ATTACK, baseLoc, 16, 2.5, 0, 2.5);
    final Vector relPartVec = new Vector(3, 0, 0);
    final int steps = 16;
    final double deltaDeg = 360.0 / steps;
    for (int i = 0; i < steps; i++) {
      final Location partLoc = baseLoc.clone().add(relPartVec);
      world.spawnParticle(Particle.SWEEP_ATTACK, partLoc, 1);
      relPartVec.rotateAroundY(deltaDeg);
    }

    baseLoc.getNearbyLivingEntities(3.5, 0, 3.5, e -> {
      if (!(e instanceof Player)) {
        return true;
      }
      return FeroPlayer.of(e.getUniqueId()).getRaceType() != raceType;
    }).forEach(SpearSkill::apply);
  }

  private static void apply(final LivingEntity entity) {
    final Vector dir = entity.getLocation().toVector().subtract(baseLoc.toVector());
    dir.normalize().multiply(2).add(new Vector(0, 0.5, 0));
    entity.setVelocity(dir);
    UtilMobs.trueDamage(entity, DamageCause.ENTITY_ATTACK, 7);
  }

}
