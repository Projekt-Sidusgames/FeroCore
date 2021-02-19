package com.gestankbratwurst.ferocore.modules.racemodule.items.elf;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemHandle;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 14.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ElfOrbHandle implements CustomItemHandle {

  @Override
  public String getKey() {
    return "ELF_ORB";
  }

  @Override
  public void handleShooting(final ProjectileLaunchEvent event, final ItemStack item) {
    item.setAmount(item.getAmount() - 1);
  }

  @Override
  public void handleProjectile(final ProjectileHitEvent event, final ItemStack item) {
    this.onOrbHit(event.getEntity().getLocation());
  }

  @Override
  public void handleAttacking(final EntityDamageByEntityEvent event, final ItemStack item) {

  }

  @Override
  public void handleDefending(final EntityDamageByEntityEvent event, final ItemStack item) {

  }

  @Override
  public void handleInteracting(final PlayerInteractEvent event, final ItemStack item) {
    final Player player = event.getPlayer();
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }
    if (feroPlayer.getRaceType() != RaceType.ELF) {
      Msg.send(player, "Rasse", "Es leuchtet schön... Keine Ahnung, was das ist.");
      return;
    }

    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.8F, 1.5F);

    final Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(0.5));
    location.setDirection(player.getEyeLocation().getDirection().multiply(1.5));
    final Snowball snowball = (Snowball) new ElfOrbBallEntity(location).getBukkitEntity();
    snowball.setShooter(player);
    new ProjectileLaunchEvent(snowball).callEvent();
  }

  @Override
  public void handleBeingClicked(final InventoryClickEvent event, final ItemStack item) {

  }

  private void onOrbHit(final Location location) {
    final World world = location.getWorld();
    world.playSound(location, Sound.ENTITY_EVOKER_CAST_SPELL, 1.4F, 1.2F);
    world.playSound(location, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 0.8F, 1.5F);
    world.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.6F, 1.5F);
    final DustOptions dustOptionsInner = new DustOptions(Color.AQUA, 1.5F);
    final DustOptions dustOptionsOuter = new DustOptions(Color.AQUA, 1.1F);
    world.spawnParticle(Particle.REDSTONE, location, 12, 2, 2, 2, 2, dustOptionsInner);
    world.spawnParticle(Particle.REDSTONE, location, 18, 4.5, 4.5, 4.5, 1, dustOptionsOuter);
    for (final LivingEntity living : location.getNearbyLivingEntities(5)) {
      if (living instanceof Player) {
        final FeroPlayer feroPlayer = FeroPlayer.of(living.getUniqueId());
        if (feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.ELF) {
          continue;
        }
      }
      this.pushTarget(living, location);
    }
    ElfOrbPickupEntity.spawn(location);
  }

  private void pushTarget(final LivingEntity entity, final Location from) {
    final Vector dir = entity.getLocation().toVector().subtract(from.toVector());
    dir.normalize().multiply(2.0D).add(new Vector(0, 0.5, 0));
    entity.setVelocity(dir);
  }

  public static ItemStack createOrb(final int amount) {
    final ItemStack orb = new ItemBuilder(Model.ELF_ORB.getItem()).name("§eKristall Orb")
        .amount(amount)
        .lore("")
        .lore("§7Wird in eine Richtung gezaubert. Beim aufkommen")
        .lore("§7werden alle Feinde weg gestoßen und es spawnen")
        .lore("§7orbs, welche einen Elfen beim aufsammeln heilen.")
        .build();
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(orb, "ELF_ORB");
    return orb;
  }

}
