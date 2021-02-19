package com.gestankbratwurst.ferocore.modules.customitems;

import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event.Result;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomItemManager {

  private final Map<String, CustomItemHandle> handleMap = new Object2ObjectOpenHashMap<>();
  private final NamespacedKey customItemTag = NameSpaceFactory.provide("CUSTOM_ITEM");

  public void registerHandle(final CustomItemHandle handle) {
    this.handleMap.put(handle.getKey(), handle);
  }

  public void tagItem(final ItemStack item, final String key) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    final ItemMeta meta = item.getItemMeta();
    if (meta == null) {
      throw new IllegalArgumentException();
    }
    meta.getPersistentDataContainer().set(this.customItemTag, PersistentDataType.STRING, key);
    item.setItemMeta(meta);
  }

  private Optional<CustomItemHandle> checkItem(final ItemStack itemStack) {
    if (itemStack == null) {
      return Optional.empty();
    }

    if (itemStack.getType() == Material.AIR) {
      return Optional.empty();
    }

    final ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      return Optional.empty();
    }
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    if (container.isEmpty()) {
      return Optional.empty();
    }

    final String tag = container.get(this.customItemTag, PersistentDataType.STRING);
    if (tag == null) {
      return Optional.empty();
    }

    final CustomItemHandle itemHandle = this.handleMap.get(tag);
    return Optional.ofNullable(itemHandle);
  }

  protected void handleAttack(final EntityDamageByEntityEvent event, final ItemStack itemStack) {
    if (event.isCancelled()) {
      return;
    }
    this.checkItem(itemStack).ifPresent(handle -> handle.handleAttacking(event, itemStack));
  }

  protected void handleDefend(final EntityDamageByEntityEvent event, final ItemStack itemStack) {
    if (event.isCancelled()) {
      return;
    }
    this.checkItem(itemStack).ifPresent(handle -> handle.handleDefending(event, itemStack));
  }

  protected void handleLaunch(final ProjectileLaunchEvent event, final ItemStack itemStack) {
    if (event.isCancelled()) {
      return;
    }
    this.checkItem(itemStack).ifPresent(handle -> handle.handleShooting(event, itemStack));
  }

  protected void handleProjectileHit(final ProjectileHitEvent event, final ItemStack itemStack) {
    if (event.isCancelled()) {
      return;
    }
    this.checkItem(itemStack).ifPresent(handle -> handle.handleProjectile(event, itemStack));
  }

  protected void handleInteract(final PlayerInteractEvent event, final ItemStack itemStack) {
    if (event.useItemInHand() == Result.DENY) {
      return;
    }
    this.checkItem(itemStack).ifPresent(handle -> handle.handleInteracting(event, itemStack));
  }

  protected void handleClick(final InventoryClickEvent event, final ItemStack itemStack) {
    if (event.isCancelled()) {
      return;
    }

    this.checkItem(itemStack).ifPresent(handle -> handle.handleBeingClicked(event, itemStack));
  }

}
