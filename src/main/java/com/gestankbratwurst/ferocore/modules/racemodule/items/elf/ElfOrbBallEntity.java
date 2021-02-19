package com.gestankbratwurst.ferocore.modules.racemodule.items.elf;

import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import net.minecraft.server.v1_16_R3.EntitySnowball;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftVector;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 14.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ElfOrbBallEntity extends EntitySnowball {

  public ElfOrbBallEntity(final Location location) {
    super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
    ((CraftWorld) location.getWorld()).getHandle().addEntity(this, SpawnReason.DEFAULT);
    this.setItem(CraftItemStack.asNMSCopy(Model.ELF_ORB_MODEL.getItem()));
    this.setMot(CraftVector.toNMS(location.getDirection()));
    this.velocityChanged = true;
    super.addScoreboardTag("ELF_ORB");
  }


  @Override
  public void tick() {
    super.tick();
    if (super.ticksLived % 2 == 0) {
      final World world = super.world.getWorld();
      final DustOptions options = new DustOptions(Color.AQUA, 1.0F);
      world.spawnParticle(Particle.REDSTONE, super.lastX, super.lastY, super.lastZ, 1, options);
    }
  }


}
