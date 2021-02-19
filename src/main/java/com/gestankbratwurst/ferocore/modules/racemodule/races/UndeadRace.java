package com.gestankbratwurst.ferocore.modules.racemodule.races;

import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UndeadRace extends Race {

  private transient Set<PotionEffectType> immuneEffects;

  public UndeadRace(final String displayName, final Model icon) {
    super(displayName, icon);
  }

  @Override
  public void init(final JavaPlugin javaPlugin) {
    this.immuneEffects = ImmutableSet.of(
        PotionEffectType.POISON,
        PotionEffectType.SLOW,
        PotionEffectType.BLINDNESS
    );
  }

  @Override
  public void onSecond(final Player player) {

  }

  @Override
  public void onAttack(final EntityDamageByEntityEvent event) {

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

  }

  @Override
  public void onDamaged(final EntityDamageEvent event) {
    final long time = event.getEntity().getWorld().getTime();
    if (time < 13000) {
      event.setDamage(event.getDamage() * 1.25);
    } else {
      event.setDamage(event.getDamage() * 0.75);
    }
  }

  @Override
  public void onShoot(final ProjectileLaunchEvent event) {

  }

  @Override
  public void onPotionEffect(final EntityPotionEffectEvent event) {
    final PotionEffect effect = event.getNewEffect();
    if (effect == null) {
      return;
    }
    if (this.immuneEffects.contains(effect.getType())) {
      event.setCancelled(true);
    }
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

  }

  @Override
  public void onLogin(final Player player) {

  }

  @Override
  public List<String> getDescription() {
    final List<String> desc = new ArrayList<>();

    desc.add("§7Diese Rasse ist untot doch immer noch");
    desc.add("§7verfeindet mit den lebenden Toten.");
    desc.add("");
    desc.add("§aRassenbonus I");
    desc.add("§7Du erleidest §e25%§7 weniger Schaden");
    desc.add("§7in der Nacht.");
    desc.add("");
    desc.add("§aRassenbonus II");
    desc.add("§7Immun gegen: ");
    desc.add("§7Gift, Verlangsamung und Blindheit");
    desc.add("");
    desc.add("§cRassenmalus");
    desc.add("§7Du erleidest §c25%§7 mehr Schaden");
    desc.add("§7am Tag.");
    desc.add("");
    desc.add("§9Spezialrezept");
    desc.add("§8Beschwörungstotem");
    desc.add("§7Kann aufgestellt werden, um Zombies");
    desc.add("§7zu beschwören, welche nur lebende");
    desc.add("§7Wesen angreifen.");
    desc.add("§7Spawnt einen Zombie alle §e2 §7Sekunden");
    desc.add("§7und insgesamt §e10 §7Zombies.");

    return desc;
  }

  @Override
  public Model getRaceLeaderCrownModel() {
    return Model.UNDEAD_CROWN;
  }
}
