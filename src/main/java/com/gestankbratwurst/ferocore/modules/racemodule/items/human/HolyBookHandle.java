package com.gestankbratwurst.ferocore.modules.racemodule.items.human;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemHandle;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.custommob.CustomMobModule;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import java.util.EnumSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.v1_16_R3.EntityTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 19.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class HolyBookHandle implements CustomItemHandle {

  private static final EnumSet<Material> VALID_PLACE_MATERIALS = EnumSet
      .of(Material.AIR, Material.CAVE_AIR, Material.GRASS, Material.TALL_GRASS);

  public HolyBookHandle() {
    CustomMobModule.registerCustomEntity("armor_stand", "holy_circle", EntityTypes.ARMOR_STAND, HolyCircleMarker::new, 1);
  }

  @Override
  public String getKey() {
    return "HOLY_BOOK";
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
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace() || feroPlayer.getRaceType() != RaceType.HUMAN) {
      Msg.send(player, "Rasse", "Du verstehst kein Wort und würdest sowieso nichts davon glauben...");
      return;
    }

    final Block targetBlock = player.getTargetBlock(64);
    if (targetBlock == null) {
      Msg.send(player, "Rasse", "Du benötigst ein näheres Ziel.");
      return;
    }
    final Block placeBlock = targetBlock.getRelative(BlockFace.UP);
    final Location placeLoc = placeBlock.getLocation().add(0.5, 0.5, 0.5);
    if (!VALID_PLACE_MATERIALS.contains(placeBlock.getType())) {
      Msg.send(player, "Rasse", "Du benötigst ein freieres Ziel.");
      return;
    }

    item.subtract();
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    final int amount = random.nextInt(3, 5);

    for (int i = 0; i < amount; i++) {
      Location location = placeLoc.clone().add(random.nextDouble(-10, 10), 0, random.nextDouble(-8, 8));
      location = location.getWorld().getHighestBlockAt(location.getBlockX(), location.getBlockZ()).getLocation();
      location.add(0.5, 1.25, 0.5);
      final Location finalLoc = location.clone();
      TaskManager.getInstance().runBukkitSyncDelayed(() -> new HolyCircleMarker(finalLoc), 5 + i * 12);
    }
  }

  @Override
  public void handleBeingClicked(final InventoryClickEvent event, final ItemStack item) {

  }

  public static ItemStack createBook() {
    final ItemStack canon = new ItemBuilder(Model.HOLY_BOOK.getItem()).name("§eHeilige Seiten")
        .lore("")
        .lore("§7Rufe nach einem Blitzschlag auf")
        .lore("§7deine Feinde. Beschwöre 2 - 4 Blitze.")
        .addPersistentData("ID", PersistentDataType.STRING, UUID.randomUUID().toString())
        .build();
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(canon, "HOLY_BOOK");
    return canon;
  }

}
