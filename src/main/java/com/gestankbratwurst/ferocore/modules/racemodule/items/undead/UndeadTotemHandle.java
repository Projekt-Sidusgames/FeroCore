package com.gestankbratwurst.ferocore.modules.racemodule.items.undead;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemHandle;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.custommob.CustomMobModule;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.EnumSet;
import net.minecraft.server.v1_16_R3.EntityTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 14.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UndeadTotemHandle implements CustomItemHandle {

  private static final EnumSet<Material> VALID_PLACE_MATERIALS = EnumSet.of(Material.AIR, Material.CAVE_AIR);

  public UndeadTotemHandle() {
    CustomMobModule.registerCustomEntity("armor_stand", "temp_stand", EntityTypes.ARMOR_STAND, UndeadTotemEntity::new, 1);
  }

  @Override
  public String getKey() {
    return "UNDEAD_TOTEM";
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
    final Player player = event.getPlayer();

    final Block block = event.getClickedBlock();
    if (block == null) {
      return;
    }

    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace() || feroPlayer.getRaceType() != RaceType.UNDEAD) {
      Msg.send(player, "Rasse", "Das möchtest du lieber nicht benutzen...");
      return;
    }

    if (!this.isViablePlaceLocation(block)) {
      Msg.send(player, "Rasse", "Kein Platz zum Platzieren.");
      return;
    }

    item.setAmount(item.getAmount() - 1);
    final Location placeLoc = block.getLocation().add(0.5, 1.1, 0.5);
    this.placeTotem(placeLoc);
  }

  @Override
  public void handleBeingClicked(final InventoryClickEvent event, final ItemStack item) {

  }

  private boolean isViablePlaceLocation(final Block clickedBlock) {
    if (!VALID_PLACE_MATERIALS.contains(clickedBlock.getRelative(0, 1, 0).getType())) {
      return false;
    }
    return VALID_PLACE_MATERIALS.contains(clickedBlock.getRelative(0, 2, 0).getType());
  }

  private void placeTotem(final Location location) {
    new UndeadTotemEntity(location, 60 * 20, Model.UNDEAD_TOTEM.getItem());
  }

  public static ItemStack createTotem() {
    final ItemStack canon = new ItemBuilder(Model.UNDEAD_TOTEM.getItem()).name("§eTotem der Unterwelt")
        .lore("")
        .lore("§7Kann platziert werden, um")
        .lore("§7Untote zu beschwören, die alle")
        .lore("§7Lebewesen angreift, welche nicht")
        .lore("§7der untoten Rasse angehören.")
        .build();
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(canon, "UNDEAD_TOTEM");
    return canon;
  }

}
