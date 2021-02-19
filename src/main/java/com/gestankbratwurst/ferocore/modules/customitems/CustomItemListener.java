package com.gestankbratwurst.ferocore.modules.customitems;

import java.util.WeakHashMap;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class CustomItemListener implements Listener {

  // TODO all ItemSlot items?
  // TODO all LivingEntities?

  private final CustomItemManager customItemManager;
  private final WeakHashMap<Projectile, ItemStack> projectileLauncherMap = new WeakHashMap<>();

  @EventHandler
  public void onLaunch(final ProjectileLaunchEvent event) {
    final Projectile projectile = event.getEntity();
    final ProjectileSource source = projectile.getShooter();
    if (!(source instanceof Player)) {
      return;
    }
    final ItemStack item = ((Player) source).getInventory().getItemInMainHand();
    this.projectileLauncherMap.put(projectile, item.clone());
    this.customItemManager.handleLaunch(event, item);
  }

  @EventHandler
  public void onHit(final ProjectileHitEvent event) {
    final ItemStack launcher = this.projectileLauncherMap.remove(event.getEntity());
    if (launcher == null) {
      return;
    }
    this.customItemManager.handleProjectileHit(event, launcher);
  }

  @EventHandler
  public void onInteract(final PlayerInteractEvent event) {
    this.customItemManager.handleInteract(event, event.getItem());
  }

  @EventHandler
  public void onInvClick(final InventoryClickEvent event) {
    final ItemStack clicked = event.getCurrentItem();
    if (clicked == null) {
      return;
    }

    this.customItemManager.handleClick(event, clicked);
  }

  @EventHandler
  public void onDamage(final EntityDamageByEntityEvent event) {
    final Entity attacker = event.getDamager();
    final Entity defender = event.getEntity();
    if (attacker instanceof Player) {
      final Player attackerPlayer = (Player) attacker;
      final ItemStack mainItem = attackerPlayer.getInventory().getItemInMainHand();
      final ItemStack secondaryItem = attackerPlayer.getInventory().getItemInOffHand();
      this.customItemManager.handleAttack(event, mainItem);
      this.customItemManager.handleAttack(event, secondaryItem);
    }
    if (defender instanceof Player) {
      final Player defenderPlayer = (Player) defender;
      final ItemStack mainItem = defenderPlayer.getInventory().getItemInMainHand();
      final ItemStack secondaryItem = defenderPlayer.getInventory().getItemInOffHand();
      this.customItemManager.handleDefend(event, mainItem);
      this.customItemManager.handleDefend(event, secondaryItem);
    }
  }

}
