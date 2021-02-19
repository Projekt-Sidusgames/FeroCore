package com.gestankbratwurst.ferocore.modules.racemodule.items.orc;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemHandle;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 17.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class OrcHornItemHandle implements CustomItemHandle {

  private final NamespacedKey HORN_CD_KEY = NameSpaceFactory.provide("ORC_HORN_CD");
  private final int cooldownInSeconds = 32;

  @Override
  public String getKey() {
    return "ORC_HORN";
  }

  @Override
  public void handleShooting(final ProjectileLaunchEvent event, final ItemStack item) {

  }

  @Override
  public void handleProjectile(final ProjectileHitEvent event, final ItemStack item) {

  }

  @Override
  public void handleAttacking(final EntityDamageByEntityEvent event, final ItemStack item) {

  }

  @Override
  public void handleDefending(final EntityDamageByEntityEvent event, final ItemStack item) {

  }

  @Override
  public void handleInteracting(final PlayerInteractEvent event, final ItemStack item) {
    final ItemMeta meta = item.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    final Long nextRefresh = container.get(this.HORN_CD_KEY, PersistentDataType.LONG);
    final long now = System.currentTimeMillis();
    if (nextRefresh == null) {
      container.set(this.HORN_CD_KEY, PersistentDataType.LONG, now + 1000 * this.cooldownInSeconds);
    } else if (nextRefresh - now > 0) {
      return;
    }
    container.set(this.HORN_CD_KEY, PersistentDataType.LONG, now + 1000 * this.cooldownInSeconds);
    item.setItemMeta(meta);
    this.playHorn(event.getPlayer().getLocation());
  }

  @Override
  public void handleBeingClicked(final InventoryClickEvent event, final ItemStack item) {

  }

  private void playHorn(final Location location) {
    CustomSound.ORC_HORN.playAt(location, SoundCategory.HOSTILE, 2F, 1F);
    for (final LivingEntity living : location.getNearbyLivingEntities(16, 8, liv -> liv.getType() != EntityType.ARMOR_STAND)) {
      if (living instanceof Player) {
        final FeroPlayer feroPlayer = FeroPlayer.of(living.getUniqueId());
        if (feroPlayer.hasChosenRace() && feroPlayer.getRaceType() == RaceType.ORC) {
          this.addBuff((Player) living);
          return;
        }
      }
      this.pushAway(living, location);
    }
  }

  private void addBuff(final Player player) {
    CustomSound.HEART_BEAT_10S.play(player);
    TaskManager.getInstance()
        .runBukkitSyncDelayed(() -> CustomSound.ORC_CRY.playAt(player.getLocation(), SoundCategory.PLAYERS, 1.5F, 1F), 20);
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 170, 1));
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    feroPlayer.addTempTag("ORC_HORN", 200);
  }

  private void pushAway(final LivingEntity entity, final Location location) {
    final Vector dir = entity.getLocation().toVector().subtract(location.toVector());
    dir.normalize().multiply(2);
    dir.setY(dir.getY() + 1);
  }

  public static ItemStack createHorn() {
    final ItemStack canon = new ItemBuilder(Model.ORC_HORN.getItem()).name("§eKriegshorn")
        .lore("")
        .lore("§7Kann benutz werden, um zum Angriff zu")
        .lore("§7blasen. Erhöht kurzzeitig das Lauftempo")
        .lore("§7naher Orks stark und verdoppelt den")
        .lore("§7Rassenbonus etwas länger.")
        .addPersistentData("ID", PersistentDataType.STRING, UUID.randomUUID().toString())
        .build();
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(canon, "ORC_HORN");
    return canon;
  }

}
