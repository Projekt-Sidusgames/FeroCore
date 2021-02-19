package com.gestankbratwurst.ferocore.modules.racemodule.items.dwarf;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemHandle;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.List;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 07.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DwarfCanonHandle implements CustomItemHandle {

  @Override
  public String getKey() {
    return "DWARF_CANON";
  }

  @Override
  public void handleShooting(final ProjectileLaunchEvent event, final ItemStack item) {

  }

  @Override
  public void handleProjectile(final ProjectileHitEvent event, final ItemStack item) {
    final String type = event.getEntity().getPersistentDataContainer()
        .get(NameSpaceFactory.provide("CANON_BALL"), PersistentDataType.STRING);
    if (type == null) {
      System.out.println("Canonball Type is null !!??!!");
      return;
    }

    CanonBallType.valueOf(type).onImpact(event, item);
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
    if (feroPlayer.getRaceType() != RaceType.DWARF) {
      Msg.send(player, "Rasse", "Du hast keine Ahnung, wie man das bedient...");
      return;
    }

    final ItemMeta meta = item.getItemMeta();
    final PersistentDataContainer canonContainer = meta.getPersistentDataContainer();
    final String currentCanonStr = canonContainer.get(NameSpaceFactory.provide("LOADED_CANON_BALL"), PersistentDataType.STRING);

    if (currentCanonStr == null) {
      return;
    }

    canonContainer.remove(NameSpaceFactory.provide("LOADED_CANON_BALL"));
    final List<String> lore = meta.getLore();
    lore.set(4, "§7Geladen: §f-");
    meta.setLore(lore);
    item.setItemMeta(meta);

    final Location startLoc = player.getEyeLocation();
    startLoc.add(startLoc.getDirection().clone().multiply(0.5));
    final Vector velocity = startLoc.getDirection().clone().multiply(1.5);

    final Snowball snowball = startLoc.getWorld().spawn(startLoc, Snowball.class);
    snowball.setItem(Model.CANON_BALL.getItem());
    snowball.setVelocity(velocity);
    snowball.setShooter(player);
    snowball.getPersistentDataContainer().set(NameSpaceFactory.provide("CANON_BALL"), PersistentDataType.STRING, currentCanonStr);
    new ProjectileLaunchEvent(snowball).callEvent();
    startLoc.getWorld().playSound(startLoc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 2F, 0.5F);
  }

  @Override
  public void handleBeingClicked(final InventoryClickEvent event, final ItemStack item) {
    final ItemStack clickedWith = event.getCursor();
    if (clickedWith == null || clickedWith.getType() == Material.AIR) {
      return;
    }

    final ItemMeta meta = clickedWith.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    final String canonBallTypeStr = container.get(NameSpaceFactory.provide("CANON_BALL"), PersistentDataType.STRING);
    if (canonBallTypeStr == null) {
      return;
    }

    event.setCancelled(true);

    final ItemMeta canonMeta = item.getItemMeta();
    final PersistentDataContainer canonContainer = canonMeta.getPersistentDataContainer();
    final String currentCanonStr = canonContainer.get(NameSpaceFactory.provide("LOADED_CANON_BALL"), PersistentDataType.STRING);

    if (currentCanonStr != null) {
      return;
    }

    final Player player = (Player) event.getWhoClicked();
    UtilPlayer.playSound(player, Sound.BLOCK_COMPARATOR_CLICK, 0.8F, 0.2F);
    canonContainer.set(NameSpaceFactory.provide("LOADED_CANON_BALL"), PersistentDataType.STRING, canonBallTypeStr);
    clickedWith.subtract();

    final List<String> lore = canonMeta.getLore();
    lore.set(4, "§7Geladen: §f" + CanonBallType.valueOf(canonBallTypeStr).displayName);
    canonMeta.setLore(lore);

    item.setItemMeta(canonMeta);
  }

  public static ItemStack createCanon() {
    final ItemStack canon = new ItemBuilder(Model.DWARF_CANON.getItem()).name("§eZwergenkanone")
        .lore("")
        .lore("§7Kann mit verschiedene Kanonen-")
        .lore("§7kugeln beladen werden.")
        .lore("")
        .lore("§7Geladen: §f-")
        .build();
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(canon, "DWARF_CANON");
    return canon;
  }

  @AllArgsConstructor
  public enum CanonBallType {

    NORMAL("Kanonenkugel") {
      @Override
      ItemStack getAsItem() {
        return new ItemBuilder(Model.CANON_BALL.getItem())
            .name("§fKanonenkugel")
            .lore("")
            .lore("§7Klicke damit auf eine Kanone,")
            .lore("§7um sie zu laden.")
            .addPersistentData("CANON_BALL", PersistentDataType.STRING, "NORMAL")
            .build();
      }

      @Override
      void onImpact(final ProjectileHitEvent event, final ItemStack item) {
        final Location location = event.getEntity().getLocation();
        location.getWorld().createExplosion(location, 2F, false);
      }
    };

    abstract ItemStack getAsItem();

    abstract void onImpact(final ProjectileHitEvent event, final ItemStack item);

    private final String displayName;
  }

}
