package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.playermodule.PlayerOptionType;
import com.gestankbratwurst.ferocore.modules.rolemodule.skills.BleedingDebuff;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.common.UtilMobs;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import net.crytec.libs.protocol.holograms.AbstractHologram;
import net.crytec.libs.protocol.holograms.impl.HologramManager;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RoleListener implements Listener {

  private static final NamespacedKey BOW_FIGHTER_KEY = NameSpaceFactory.provide("BOW_FIGHTER_LVL");
  private static final NamespacedKey ARROW_SCALE_KEY = NameSpaceFactory.provide("ARROW_SCALE");
  private static final NamespacedKey POTION_KEY = NameSpaceFactory.provide("POTION_LVL");
  private static final Vector DMG_VECTOR = new Vector(0, 0.15, 0);
  private final HologramManager hologramManager;
  private final ThreadLocalRandom random = ThreadLocalRandom.current();

  @EventHandler(priority = EventPriority.HIGH)
  public void onDamage(final EntityDamageEvent event) {
    if (event.isCancelled()) {
      return;
    }
    final Location baseLoc = event.getEntity().getLocation()
        .add(this.random.nextDouble(-0.5, 0.5), 0.5 + this.random.nextDouble(-0.5, 0.5), this.random.nextDouble(-0.5, 0.5));
    final double dmg = (int) (event.getDamage() * 10.0) / 10.0;
    final AbstractHologram hologram = this.hologramManager
        .createHologram(baseLoc, pl -> FeroPlayer.of(pl).getPlayerOptions().isEnabled(PlayerOptionType.SHOW_DMG_HOLOGRAMS));
    hologram.appendTextLine("§6-" + dmg);
    this.hologramManager.decorateAsMoving(hologram, DMG_VECTOR, 30);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onHeal(final EntityRegainHealthEvent event) {
    if (event.isCancelled()) {
      return;
    }
    final Location baseLoc = event.getEntity().getLocation()
        .add(this.random.nextDouble(-0.5, 0.5), 0.5 + this.random.nextDouble(-0.5, 0.5), this.random.nextDouble(-0.5, 0.5));
    final double heal = (int) (event.getAmount() * 10.0) / 10.0;
    final AbstractHologram hologram = this.hologramManager
        .createHologram(baseLoc, pl -> FeroPlayer.of(pl).getPlayerOptions().isEnabled(PlayerOptionType.SHOW_DMG_HOLOGRAMS));
    hologram.appendTextLine("§a+" + heal);
    this.hologramManager.decorateAsMoving(hologram, DMG_VECTOR, 30);
  }

  @EventHandler
  public void onExpGain(final PlayerExpChangeEvent event) {
    FeroPlayer.of(event.getPlayer()).addRoleExp(Math.max(0, event.getAmount()));
  }

  @EventHandler
  public void onPotionSplash(final PotionSplashEvent event) {
    final PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
    final Integer potionLvl = container.get(POTION_KEY, PersistentDataType.INTEGER);
    if (potionLvl == null || potionLvl < 0) {
      event.setCancelled(true);
      return;
    }
    if (potionLvl < 10) {
      return;
    }
    final double dmg = ThreadLocalRandom.current().nextDouble(2, 4);
    event.getAffectedEntities().forEach(entity -> UtilMobs.trueDamage(entity, DamageCause.MAGIC, dmg));
  }

  @EventHandler
  public void onShoot(final EntityShootBowEvent event) {
    final LivingEntity shooter = event.getEntity();
    if (!(shooter instanceof Player)) {
      return;
    }
    final Player player = (Player) event.getEntity();
    final Entity projectile = event.getProjectile();
    if (projectile instanceof Arrow) {
      this.handleArrowLaunch(event, player, (Projectile) projectile);
    }
  }

  @EventHandler
  public void onLaunch(final PlayerLaunchProjectileEvent event) {
    final Projectile projectile = event.getProjectile();
    if (projectile instanceof ThrownPotion) {
      this.handlePotionThrow(event, projectile);
    }
  }

  private void handlePotionThrow(final PlayerLaunchProjectileEvent event, final Projectile projectile) {
    final PersistentDataContainer container = projectile.getPersistentDataContainer();
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    final boolean potionFighter = feroPlayer.hasChosenRole() && (feroPlayer.getChosenRoleTye() == RoleType.SHAMAN
        || feroPlayer.getChosenRoleTye() == RoleType.ALCHEMIST);
    final int potionLvl = potionFighter ? feroPlayer.getRoleLevel() : -1;
    container.set(POTION_KEY, PersistentDataType.INTEGER, potionLvl);
  }

  private void handleArrowLaunch(final EntityShootBowEvent event, final Player player, final Projectile projectile) {
    final PersistentDataContainer container = projectile.getPersistentDataContainer();
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    final boolean bowFighter = feroPlayer.hasChosenRole() && feroPlayer.getChosenRoleTye() == RoleType.BOW_FIGHTER;
    final int bowFighterLevel = bowFighter ? feroPlayer.getRoleLevel() : -1;
    container.set(BOW_FIGHTER_KEY, PersistentDataType.INTEGER, bowFighterLevel);
    container.set(ARROW_SCALE_KEY, PersistentDataType.DOUBLE,
        RoleType.BOW_FIGHTER.getDamageArmorScale(player.getInventory().getArmorContents()));
    if (bowFighterLevel < 10) {
      return;
    }
    TaskManager.getInstance().runBukkitSync(() -> projectile.setVelocity(projectile.getVelocity().multiply(1.33)));
    projectile.getWorld().playSound(projectile.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.25F, 2F);
  }

  private void handleProjectile(final EntityDamageByEntityEvent event, final Projectile projectile) {
    final PersistentDataContainer container = projectile.getPersistentDataContainer();
    final Integer lvl = container.get(BOW_FIGHTER_KEY, PersistentDataType.INTEGER);
    final Double dmgScalar = container.get(ARROW_SCALE_KEY, PersistentDataType.DOUBLE);
    if (dmgScalar != null) {
      event.setDamage(event.getDamage() * dmgScalar);
    }

    if (lvl == null) {
      return;
    }
    if (lvl < 0) {
      event.setDamage(event.getDamage() * 0.5);
    }
    if (lvl < 1) {
      return;
    }
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    final Entity damaged = event.getEntity();
    if (!(damaged instanceof LivingEntity)) {
      return;
    }
    final LivingEntity damagedLiving = (LivingEntity) damaged;
    if (random.nextDouble() < 0.16) {
      new BleedingDebuff(damagedLiving).start();
    }
    if (lvl < 20) {
      return;
    }
    UtilMobs.trueDamage(damagedLiving, DamageCause.PROJECTILE, 4.0);
    damagedLiving.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 25, 0));
    damagedLiving.getWorld().spawnParticle(Particle.CRIT, projectile.getLocation(), 6, 0.1, 0.1, 0.1);
  }

  @EventHandler
  public void onAttack(final EntityDamageByEntityEvent event) {
    final Entity attacker = event.getDamager();
    if (attacker instanceof Projectile) {
      this.handleProjectile(event, (Projectile) event.getDamager());
      return;
    } else if (!(attacker instanceof Player)) {
      return;
    }
    final Entity defender = event.getEntity();
    final Player player = (Player) attacker;
    final ItemStack item = player.getInventory().getItemInMainHand();
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    double scalar = 0.2;

    if (feroPlayer.hasChosenRole()) {
      final RoleType roleType = feroPlayer.getChosenRoleTye();
      if (roleType.isSuitableWeapon(item)) {
        scalar = 1.0;
        if (defender instanceof LivingEntity) {
          feroPlayer.getChosenRoleTye().handleAttacking(event, feroPlayer.getRoleLevel(), player, (LivingEntity) defender);
        }
      } else {
        scalar = 0.25;
      }
      final double armorScalar = roleType.getDamageArmorScale(player.getInventory().getArmorContents());
      scalar *= armorScalar;
    }

    event.setDamage(event.getDamage() * scalar);

    if (defender instanceof Player) {
      final Player defendingPlayer = (Player) defender;
      final FeroPlayer deFero = FeroPlayer.of(defendingPlayer);
      if (deFero.hasChosenRole() && deFero.getChosenRoleTye() == RoleType.SPEAR_FIGHTER && deFero.getRoleLevel() > 9) {
        if (ThreadLocalRandom.current().nextDouble() < 10) {
          event.setDamage(0);
          defendingPlayer.getWorld().playSound(defendingPlayer.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1F, 0.8F);
          defendingPlayer.getWorld().playSound(defendingPlayer.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1F, 1.2F);
        }
      }
    }
  }

}
