package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent;
import com.destroystokyo.paper.event.entity.PhantomPreSpawnEvent;
import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent;
import com.destroystokyo.paper.event.entity.TurtleLayEggEvent;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.playermodule.PlayerModule;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.UtilModule;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.Optional;
import java.util.UUID;
import net.crytec.libs.protocol.holograms.AbstractHologram;
import net.crytec.libs.protocol.holograms.impl.HologramManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 02.07.2020
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ProtectionListener implements Listener {

  public ProtectionListener(final ProtectionModule protectionManager) {
    this.dataModule = FeroCore.getModule(PlayerModule.class);
    this.protectionManager = protectionManager;
    this.lastXTime = new Object2LongOpenHashMap<>();
    this.hologramManager = FeroCore.getModule(UtilModule.class).getHologramManager();
    this.taskManager = TaskManager.getInstance();
  }

  private final PlayerModule dataModule;
  private final TaskManager taskManager;
  private final HologramManager hologramManager;
  private final ProtectionModule protectionManager;
  private final Object2LongMap<UUID> lastXTime;

  // TODO make util method in PSSPlayer
  private void showX(final FeroPlayer feroPlayer, final String message) {
    final long last = this.lastXTime.getLong(feroPlayer.getPlayerID());
    if (System.currentTimeMillis() - last > 750) {
      this.lastXTime.put(feroPlayer.getPlayerID(), System.currentTimeMillis());
      final Optional<Player> optionalPlayer = feroPlayer.getOnlinePlayer();
      if (optionalPlayer.isEmpty()) {
        throw new IllegalStateException();
      }
      final Player player = optionalPlayer.get();
      final Location eyes = player.getEyeLocation();
      eyes.add(eyes.getDirection().multiply(1.5));
      final AbstractHologram hologram = this.hologramManager.createHologram(eyes);
      hologram.appendTextLine("" + Model.RED_X.getChar());
      this.taskManager.runBukkitSyncDelayed(hologram::delete, 20L);
      Msg.send(player, "Protection", message);
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    this.lastXTime.removeLong(event.getPlayer().getUniqueId());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final AnvilDamagedEvent event) {
    final Location loc = event.getInventory().getLocation();
    if (loc == null) {
      return;
    }
    if (this.protectionManager.getEnvironmentRules(loc.getBlock()).getState(ProtectionRule.DAMAGE_ANVIL) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockDestroyEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.SERVER_DESTROY_BLOCK) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final TNTPrimeEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.TNT_PRIME) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EndermanAttackPlayerEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENDERMAN_AGGRESSION)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityKnockbackByEntityEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_KNOCKBACK)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityTeleportEndGatewayEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.END_GATEWAY)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PhantomPreSpawnEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getSpawnLocation().getBlock()).getState(ProtectionRule.PHANTOM_SPAWN)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityJumpEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_JUMP)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final TurtleLayEggEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_JUMP)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerElytraBoostEvent event) {
    final Block block = event.getPlayer().getLocation().getBlock();
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(block, feroPlayer).getState(ProtectionRule.ELYTRA_BOOST) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nicht boosten.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerJumpEvent event) {
    final Block block = event.getPlayer().getLocation().getBlock();
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(block, feroPlayer).getState(ProtectionRule.PLAYER_JUMP) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nicht springen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerLaunchProjectileEvent event) {
    final Block block = event.getPlayer().getLocation().getBlock();
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(block, feroPlayer).getState(ProtectionRule.PLAYER_LAUNCH_PROJECTILE) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Projektile shießen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerPickupExperienceEvent event) {
    final Block block = event.getPlayer().getLocation().getBlock();
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(block, feroPlayer).getState(ProtectionRule.PLAYER_PICKUP_EXP) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Exp aufheben.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockBreakEvent event) {
    final Block block = event.getBlock();
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(block, feroPlayer).getState(ProtectionRule.BREAK_BLOCK) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Blöcke abbauen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockBurnEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.BLOCK_BURN) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockCanBuildEvent event) {
    final Block block = event.getBlock();
    if (event.getPlayer() == null) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(block, feroPlayer).getState(ProtectionRule.BLOCK_CHECK_BUILD) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nicht bauen.");
      event.setBuildable(false);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockDamageEvent event) {
    final Block block = event.getBlock();
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(block, feroPlayer).getState(ProtectionRule.BLOCK_DAMAGE) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Blöcke beschädigen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockDispenseEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.BLOCK_DISPENSE) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockDropItemEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getBlock(), feroPlayer).getState(ProtectionRule.BLOCK_DROP_ITEM) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Items fallen lassen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockExplodeEvent event) {
    event.blockList()
        .removeIf(block -> this.protectionManager.getEnvironmentRules(block).getState(ProtectionRule.BLOCK_EXPLODE) == RuleState.DENY);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockFadeEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.BLOCK_FADES) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockFertilizeEvent event) {
    if (event.getPlayer() == null) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getBlock(), feroPlayer).getState(ProtectionRule.BLOCK_FERTILIZE) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nicht düngen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockFormEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.BLOCK_FORM) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockGrowEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.BLOCK_GROW) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockIgniteEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.BLOCK_IGNITE) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockPistonExtendEvent event) {
    for (final Block block : event.getBlocks()) {
      if (this.protectionManager.getEnvironmentRules(block).getState(ProtectionRule.BLOCK_PISTON) == RuleState.DENY) {
        event.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockPistonRetractEvent event) {
    for (final Block block : event.getBlocks()) {
      if (this.protectionManager.getEnvironmentRules(block).getState(ProtectionRule.BLOCK_PISTON) == RuleState.DENY) {
        event.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockPlaceEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getBlock(), feroPlayer).getState(ProtectionRule.BLOCK_PLACE) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Blöcke platzieren.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final BlockSpreadEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.BLOCK_SPREAD) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityBlockFormEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.ENTITY_FORMS_BLOCK) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final SignChangeEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getBlock(), feroPlayer).getState(ProtectionRule.SIGN_CHANGE) == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Schilder verändern.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final CreatureSpawnEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getLocation().getBlock()).getState(ProtectionRule.CREATURE_SPAWN)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PreCreatureSpawnEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getSpawnLocation().getBlock()).getState(ProtectionRule.CREATURE_SPAWN)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PreSpawnerSpawnEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getSpawnLocation().getBlock()).getState(ProtectionRule.CREATURE_SPAWN)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final SpawnerSpawnEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getLocation().getBlock()).getState(ProtectionRule.CREATURE_SPAWN)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityAirChangeEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.AIR_CHANGE)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityBreakDoorEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_BREAK_DOOR)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityBreedEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_BREED)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityChangeBlockEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.ENTITY_CHANGE_BLOCK)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityDamageByBlockEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock())
        .getState(ProtectionRule.ENTITY_DAMAGE_BY_BLOCK)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityDamageByEntityEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock())
        .getState(ProtectionRule.ENTITY_DAMAGE_BY_ENTITY)
        == RuleState.DENY
        ||
        this.protectionManager.getEnvironmentRules(event.getDamager().getLocation().getBlock())
            .getState(ProtectionRule.ENTITY_DAMAGE_BY_ENTITY)
            == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityDamageEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock())
        .getState(ProtectionRule.ENTITY_DAMAGE_GENERAL)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityDropItemEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_DROP_ITEM)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityExplodeEvent event) {
    event.blockList()
        .removeIf(block -> this.protectionManager.getEnvironmentRules(block).getState(ProtectionRule.ENTITY_EXPLODE) == RuleState.DENY);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityInteractEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getBlock()).getState(ProtectionRule.ENTITY_INTERACT) == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityPickupItemEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getItem().getLocation().getBlock()).getState(ProtectionRule.ENTITY_PICKUP_ITEM)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityPotionEffectEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_POTION_EFFECT)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityRegainHealthEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_REGAIN_HEALTH)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityResurrectEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_RESURRECT)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityShootBowEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_SHOOT_BOW)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityTameEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_TAME)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityToggleGlideEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_GLIDE)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final EntityToggleSwimEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getEntity().getLocation().getBlock()).getState(ProtectionRule.ENTITY_SWIM)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final FoodLevelChangeEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getEntity().getUniqueId());
    if (this.protectionManager.getPlayerRules(event.getEntity().getLocation().getBlock(), feroPlayer)
        .getState(ProtectionRule.SIGN_CHANGE)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final ItemDespawnEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getLocation().getBlock()).getState(ProtectionRule.ITEM_DESPAWN)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final ProjectileLaunchEvent event) {
    if (this.protectionManager.getEnvironmentRules(event.getLocation().getBlock()).getState(ProtectionRule.ITEM_DESPAWN)
        == RuleState.DENY) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final AsyncPlayerChatEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getPlayer().getLocation().getBlock(), feroPlayer)
        .getState(ProtectionRule.PLAYER_CHAT)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nichts schreiben.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerArmorStandManipulateEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getRightClicked().getLocation().getBlock(), feroPlayer)
        .getState(ProtectionRule.PLAYER_ARMORSTAND)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Armorstands verändern.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerAttemptPickupItemEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getItem().getLocation().getBlock(), feroPlayer)
        .getState(ProtectionRule.PLAYER_PICKUP_ITEM)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Items aufheben.");
      event.setCancelled(true);
    }
  }

  // TODO use all sub events
//  @EventHandler(priority = EventPriority.LOWEST)
//  public void onEvent(PlayerBucketEvent event) {
//    PSSPlayer PSSPlayer = avarionDataManager.getOnlineData(event.getPlayer().getUniqueId());
//    if (protectionManager.getPlayerRules(event.getBlock(), PSSPlayer)
//        .getState(ProtectionRule.PLAYER_BUCKET)
//        == RuleState.DENY) {
//      event.setCancelled(true);
//    }
//  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerDropItemEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getPlayer().getLocation().getBlock(), feroPlayer)
        .getState(ProtectionRule.PLAYER_DROP_ITEM)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Items droppen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerFishEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getHook().getLocation().getBlock(), feroPlayer)
        .getState(ProtectionRule.PLAYER_FISH)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nicht fischen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerInteractAtEntityEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getRightClicked().getLocation().getBlock(), feroPlayer)
        .getState(ProtectionRule.PLAYER_INTERACT_ENTITY)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nicht mit Entities interagieren.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerInteractEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (event.getClickedBlock() == null || !(event.getAction() == Action.PHYSICAL)) {
      return;
    }
    if (this.protectionManager.getPlayerRules(event.getClickedBlock(), feroPlayer).getState(ProtectionRule.PLAYER_INTERACT)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier nicht interagieren.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerPortalEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getFrom().getBlock(), feroPlayer).getState(ProtectionRule.PLAYER_PORTAL)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst hier keine Portale benutzen.");
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEvent(final PlayerTeleportEvent event) {
    final FeroPlayer feroPlayer = FeroPlayer.of(event.getPlayer());
    if (this.protectionManager.getPlayerRules(event.getFrom().getBlock(), feroPlayer).getState(ProtectionRule.PLAYER_PORTAL)
        == RuleState.DENY) {
      this.showX(feroPlayer, "Du kannst dich von hier nicht fort teleportieren.");
      event.setCancelled(true);
    }
  }

}