package com.gestankbratwurst.ferocore.modules.racemodule.items.elf;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
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
public class ElfOrbPickupEntity extends EntityItem {

  public static ElfOrbPickupEntity spawn(final Location location) {
    return new ElfOrbPickupEntity(location);
  }

  private ElfOrbPickupEntity(final Location location) {
    super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(),
        CraftItemStack.asNMSCopy(Model.ELF_ORB_MODEL.getItem()));
    ((CraftWorld) location.getWorld()).getHandle().addEntity(this, SpawnReason.DEFAULT);
  }

  private boolean pickedUp = false;

  @Override
  public void tick() {
    super.tick();
    final DustOptions options = new DustOptions(Color.AQUA, 1);
    this.world.getWorld().spawnParticle(Particle.REDSTONE, super.lastX, super.lastY, super.lastZ, 1, 1, 1, 1, options);
  }

  @Override
  public void pickup(final EntityHuman human) {
    final FeroPlayer feroPlayer = FeroPlayer.of(human.getUniqueID());
    if (!feroPlayer.hasChosenRace() || feroPlayer.getRaceType() != RaceType.ELF) {
      return;
    }
    if (this.pickedUp) {
      return;
    }
    this.pickedUp = true;
    final Player player = Bukkit.getPlayer(human.getUniqueID());
    if (player == null) {
      return;
    }
    final double maxHp = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    player.setHealth(Math.min(player.getHealth() + 2.0, maxHp));
    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, 0.65F);
    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation(), 12, 1.0, 2.0, 1.0);
    this.die();
  }

}
