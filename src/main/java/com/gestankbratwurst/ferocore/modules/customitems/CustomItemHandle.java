package com.gestankbratwurst.ferocore.modules.customitems;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface CustomItemHandle {

  String getKey();

  void handleShooting(ProjectileLaunchEvent event, ItemStack item);

  void handleProjectile(ProjectileHitEvent event, ItemStack item);

  void handleAttacking(EntityDamageByEntityEvent event, ItemStack item);

  void handleDefending(EntityDamageByEntityEvent event, ItemStack item);

  void handleInteracting(PlayerInteractEvent event, ItemStack item);

  void handleBeingClicked(InventoryClickEvent event, ItemStack item);

}