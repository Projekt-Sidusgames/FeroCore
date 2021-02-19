package com.gestankbratwurst.ferocore.modules.racemodule.races;

import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
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
public class ElfRace extends Race {

  private transient String forestTag;
  private transient AttributeModifier forestMod;
  private transient Set<Biome> woodLandBiome;

  public ElfRace(final String displayName, final Model icon) {
    super(displayName, icon);
  }

  @Override
  public void init(final JavaPlugin javaPlugin) {
    this.forestTag = "EL_FOREST";
    this.forestMod = new AttributeModifier(UUID.randomUUID(), this.forestTag, 0.12, Operation.MULTIPLY_SCALAR_1);
    this.woodLandBiome = EnumSet.of(
        Biome.FOREST,
        Biome.BIRCH_FOREST,
        Biome.FLOWER_FOREST,
        Biome.TALL_BIRCH_FOREST,
        Biome.DARK_FOREST,
        Biome.BIRCH_FOREST_HILLS,
        Biome.DARK_FOREST_HILLS,
        Biome.FOREST,
        Biome.TAIGA,
        Biome.TAIGA_MOUNTAINS,
        Biome.GIANT_SPRUCE_TAIGA,
        Biome.GIANT_TREE_TAIGA
    );
  }

  @Override
  public void onSecond(final Player player) {
    final Set<String> tags = player.getScoreboardTags();

    final AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    if (speedAttr == null) {
      return;
    }

    final Biome biome = player.getLocation().getBlock().getBiome();
    if (this.woodLandBiome.contains(biome)) {
      if (!tags.contains(this.forestTag)) {
        speedAttr.addModifier(this.forestMod);
        tags.add(this.forestTag);
      }
    } else if (tags.contains(this.forestTag)) {
      speedAttr.removeModifier(this.forestMod);
      tags.remove(this.forestTag);
    }

  }

  @Override
  public void onAttack(final EntityDamageByEntityEvent event) {
    Entity attacker = event.getDamager();
    if (attacker instanceof Projectile) {
      final Projectile projectile = (Projectile) attacker;
      final ProjectileSource source = projectile.getShooter();
      if (source instanceof Player) {
        attacker = (Player) source;
      }
    }
    if (attacker.getScoreboardTags().contains(this.forestTag)) {
      event.setDamage(event.getDamage() * 1.15);
    }
  }

  @Override
  public void onBlockBreak(final BlockBreakEvent event) {

  }

  @Override
  public void onBlockDrop(final BlockDropItemEvent event) {

  }

  @Override
  public void onConsume(final PlayerItemConsumeEvent event) {

  }

  @Override
  public void onDefend(final EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Projectile)) {
      event.setDamage(event.getDamage() * 1.10);
    }
  }

  @Override
  public void onDamaged(final EntityDamageEvent event) {

  }

  @Override
  public void onShoot(final ProjectileLaunchEvent event) {

  }

  @Override
  public void onPotionEffect(final EntityPotionEffectEvent event) {

  }

  @Override
  public void onInteract(final PlayerInteractAtEntityEvent event) {

  }

  @Override
  public boolean canEquip(final Player who, final ItemStack item) {
    return true;
  }

  @Override
  public void onLogout(final Player player) {
    final AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    if (speedAttr != null) {
      speedAttr.removeModifier(this.forestMod);
      player.getScoreboardTags().remove(this.forestTag);
    }
  }

  @Override
  public void onLogin(final Player player) {

  }

  @Override
  public List<String> getDescription() {
    final List<String> desc = new ArrayList<>();

    desc.add("§7Diese Rasse spezielisiert sich");
    desc.add("§7auf das Leben im Wald.");
    desc.add("");
    desc.add("§aRassenbonus I");
    desc.add("§e12% §7Lauftempo in Waldbiomen.");
    desc.add("§7(Jungle ausgeschlossen)");
    desc.add("");
    desc.add("§aRassenbonus II");
    desc.add("§7Dein gesamter Schaden ist in Wald-");
    desc.add("§7und Wiesenbiomen um §e15% §7höher.");
    desc.add("");
    desc.add("§cRassenmalus");
    desc.add("§7Erleiden §c10% §7mehr Schaden im Nahkampf.");
    desc.add("");
    desc.add("§9Spezialrezept");
    desc.add("§8Heilquelle");
    desc.add("§7Eine Quelle, welche aufgestellt werden kann");
    desc.add("§7und alle Verbündeten in einem Umkreis von §e12");
    desc.add("§7Blöcken jede Sekunde um §e1 §7heilt.");
    desc.add("§7Außerdem erleiden sie §e10% §7weniger Schaden.");
    desc.add("§7Hält §e180s");

    return desc;
  }

  @Override
  public Model getRaceLeaderCrownModel() {
    return Model.ELF_CROWN;
  }
}
