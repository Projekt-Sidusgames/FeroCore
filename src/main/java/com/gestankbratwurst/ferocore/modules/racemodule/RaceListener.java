package com.gestankbratwurst.ferocore.modules.racemodule;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.common.UtilBlock;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RaceListener implements Listener {

  private final EvenDistributedPlayerTicker raceTicker;
  private final NamespacedKey dropValidationKey = NameSpaceFactory.provide("VALID_DROP");

  @EventHandler
  public void onEntityDrop(final EntityDropItemEvent event) {
    final PersistentDataContainer container = event.getItemDrop().getPersistentDataContainer();
    container.set(this.dropValidationKey, PersistentDataType.INTEGER, 0);
  }

  @EventHandler
  public void onPickup(final EntityPickupItemEvent event) {
    final LivingEntity entity = event.getEntity();
    if (!(entity instanceof Player)) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(entity.getUniqueId());
    if (!feroPlayer.hasChosenRace()) {
      return;
    }

    final PersistentDataContainer container = event.getItem().getPersistentDataContainer();
    if (!container.has(this.dropValidationKey, PersistentDataType.INTEGER)) {
      return;
    }
    feroPlayer.getRace().handleQuestItemPickup(event.getItem(), (Player) entity);
  }

  @EventHandler
  public void onKill(final EntityDeathEvent event) {
    final LivingEntity entity = event.getEntity();
    if (entity instanceof Player) {
      return;
    }
    final Player killer = entity.getKiller();
    if (killer == null) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(killer);
    if (!feroPlayer.hasChosenRace()) {
      return;
    }
    feroPlayer.getRace().handleQuestEntityKill(entity, killer);
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRace()) {
      final Race race = feroPlayer.getRace();
      race.onLogin(player);
      race.setAsOnline(player);
    } else {
      Msg.send(event.getPlayer(), "Rasse", "Du hast noch keine Rasse gewählt.");
      Msg.send(event.getPlayer(), "Rasse", "Benutze " + Msg.elem("/rasse") + ", um eine Rasse zu wählen.");
    }
    if (!player.getScoreboardTags().contains("SAVE_LOGOUT")) {
      final AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
      if (speedAttr != null) {
        speedAttr.getModifiers().forEach(speedAttr::removeModifier);
      }
    }
    this.raceTicker.add(player.getUniqueId());
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    final Player player = event.getPlayer();
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRace()) {
      final Race race = feroPlayer.getRace();
      race.setAsOffline(player.getUniqueId());
      race.onLogout(player);
    }
    this.raceTicker.remove(player.getUniqueId());
    player.getScoreboardTags().add("SAVE_LOGOUT");
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onChat(final AsyncPlayerChatEvent event) {
    if (event.isCancelled()) {
      return;
    }
    event.setCancelled(true);
    final UUID playerID = event.getPlayer().getUniqueId();
    final String msg = event.getMessage();
    TaskManager.getInstance().runBukkitSync(() -> {
      final FeroPlayer feroPlayer = FeroPlayer.of(playerID);
      if (feroPlayer.hasChosenRace()) {
        final Player player = Bukkit.getPlayer(playerID);
        if (player != null) {
          feroPlayer.getRace().onChat(feroPlayer, player, msg);
        }
      }
    });
  }

  @EventHandler
  public void onAttack(final EntityDamageByEntityEvent event) {
    Entity attackerEntity = event.getDamager();
    final Entity defenderEntity = event.getDamager();

    if (attackerEntity instanceof Projectile) {
      final Optional<Entity> optAttacker = this.getShooter((Projectile) attackerEntity);
      if (optAttacker.isEmpty()) {
        return;
      }
      attackerEntity = optAttacker.get();
    }

    if (attackerEntity instanceof Player) {
      final Player attackerPlayer = (Player) attackerEntity;
      final FeroPlayer feroAttacker = FeroPlayer.of(attackerPlayer);
      if (feroAttacker.hasChosenRace()) {
        feroAttacker.getRace().onAttack(event);
      }
    }
    if (defenderEntity instanceof Player) {
      final Player defenderPlayer = (Player) defenderEntity;
      final FeroPlayer feroDefender = FeroPlayer.of(defenderPlayer);
      if (feroDefender.hasChosenRace()) {
        feroDefender.getRace().onDefend(event);
      }
    }
  }

  @EventHandler
  public void onDamaged(final EntityDamageEvent event) {
    final Entity defender = event.getEntity();
    if (!(defender instanceof Player)) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(defender.getUniqueId());
    if (feroPlayer.hasChosenRace()) {
      feroPlayer.getRace().onDamaged(event);
    }
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (feroPlayer.hasChosenRace()) {
      final Block block = event.getBlock();
      if (UtilBlock.isPlayerPlaced(block)) {
        return;
      }
      feroPlayer.getRace().onBlockBreak(event);
    }
  }

  @EventHandler
  public void onBlockDrop(final BlockDropItemEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (feroPlayer.hasChosenRace() && !UtilBlock.isPlayerPlaced(event.getBlock())) {
      event.getItems().forEach(drop -> {
        final PersistentDataContainer container = drop.getPersistentDataContainer();
        container.set(this.dropValidationKey, PersistentDataType.INTEGER, 0);
      });
      feroPlayer.getRace().onBlockDrop(event);
    }
  }

  @EventHandler
  public void onConsume(final PlayerItemConsumeEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (feroPlayer.hasChosenRace()) {
      feroPlayer.getRace().onConsume(event);
    }
  }

  @EventHandler
  public void onInteract(final PlayerInteractAtEntityEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (feroPlayer.hasChosenRace()) {
      feroPlayer.getRace().onInteract(event);
    }
  }

  @EventHandler
  public void onInteract(final PlayerInteractEvent event) {
    final Player player = event.getPlayer();
    final ItemStack item = event.getItem();
    if (item == null || event.getHand() == null) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRace()) {
      if (!feroPlayer.getRace().canEquip(player, item)) {
        event.setCancelled(true);
        player.getWorld().dropItemNaturally(player.getLocation(), item);
        final EquipmentSlot slot = event.getHand();
        player.getEquipment().setItem(slot, null);
      }
    }
  }

  @EventHandler
  public void onArmor(final PlayerArmorChangeEvent event) {
    final Player player = event.getPlayer();
    final ItemStack item = event.getNewItem();
    if (item == null || item.getType() == Material.AIR) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRace()) {
      if (!feroPlayer.getRace().canEquip(player, item)) {
        player.getWorld().dropItemNaturally(player.getLocation(), item);
        final SlotType type = event.getSlotType();
        player.getEquipment().setItem(EquipmentSlot.valueOf(type.toString()), null);
      }
    }
  }

  @EventHandler
  public void onPotionEffect(final EntityPotionEffectEvent event) {
    final Entity entity = event.getEntity();
    if (!(entity instanceof Player)) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(entity.getUniqueId());
    if (feroPlayer.hasChosenRace()) {
      feroPlayer.getRace().onPotionEffect(event);
    }
  }

  @EventHandler
  public void onLaunch(final ProjectileLaunchEvent event) {
    final Optional<Entity> optionalShooter = this.getShooter(event.getEntity());
    if (optionalShooter.isEmpty()) {
      return;
    }
    final Entity shooter = optionalShooter.get();
    if (!(shooter instanceof Player)) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(shooter.getUniqueId());
    if (feroPlayer.hasChosenRace()) {
      feroPlayer.getRace().onShoot(event);
    }
  }

  private Optional<Entity> getShooter(final Projectile projectile) {
    final ProjectileSource source = projectile.getShooter();
    if (source instanceof Entity) {
      return Optional.of((Entity) source);
    }
    return Optional.empty();
  }

}
