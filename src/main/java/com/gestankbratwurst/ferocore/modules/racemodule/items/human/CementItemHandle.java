package com.gestankbratwurst.ferocore.modules.racemodule.items.human;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemHandle;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import java.util.EnumSet;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 15.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CementItemHandle implements CustomItemHandle {

  private static final EnumSet<Material> REPLACEABLE_MATERIALS = EnumSet.of(Material.AIR, Material.GRASS, Material.TALL_GRASS);

  @Override
  public String getKey() {
    return "CEMENT";
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
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    event.setUseItemInHand(Result.DENY);
    event.setUseInteractedBlock(Result.DENY);
    event.setCancelled(true);

    final Player player = event.getPlayer();

    final Block block = event.getClickedBlock();
    if (block == null) {
      return;
    }

    final Block placeBlock = REPLACEABLE_MATERIALS.contains(block.getType()) ? block : block.getRelative(event.getBlockFace());

    if (!REPLACEABLE_MATERIALS.contains(placeBlock.getType())) {
      Msg.send(player, "Rasse", "Kein Platz zum Platzieren.");
      return;
    }

    item.setAmount(item.getAmount() - 1);
    placeBlock.getWorld().playSound(placeBlock.getLocation(), Sound.BLOCK_STONE_PLACE, 0.8F, 1.2F);
    TaskManager.getInstance().runBukkitSync(() -> placeBlock.setType(Material.MUSHROOM_STEM));
  }

  @Override
  public void handleBeingClicked(final InventoryClickEvent event, final ItemStack item) {

  }

  public static ItemStack createCement(final int amount) {
    final ItemStack canon = new ItemBuilder(Model.CEMENT_BALL.getItem()).name("§fVerputz")
        .lore("")
        .lore("§7Platziere für eine weiße Wand.")
        .amount(amount)
        .build();
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(canon, "CEMENT");
    return canon;
  }

}
