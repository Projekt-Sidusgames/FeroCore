package com.gestankbratwurst.ferocore.modules.racemodule.races;

import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
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

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class HumanRace extends Race {

  private transient Set<Material> cookedFoods;

  public HumanRace(final String displayName, final Model icon) {
    super(displayName, icon);
  }

  @Override
  public void init(final JavaPlugin javaPlugin) {
    this.cookedFoods = EnumSet.of(
        Material.COOKED_PORKCHOP,
        Material.COOKED_BEEF,
        Material.COOKED_SALMON,
        Material.COOKED_MUTTON,
        Material.PUMPKIN_PIE,
        Material.BEETROOT_SOUP,
        Material.MUSHROOM_STEW,
        Material.RABBIT_STEW,
        Material.SUSPICIOUS_STEW
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
    final Material consumedMaterial = event.getItem().getType();
    if (this.cookedFoods.contains(consumedMaterial)) {
      final Player player = event.getPlayer();
      final double baseHeal = 2;
      final double maxHp = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
      final double lostHp = maxHp - player.getHealth();
      final double heal = baseHeal + 0.2 * lostHp;
      if (heal > lostHp) {
        player.setHealth(maxHp);
      } else {
        player.setHealth(player.getHealth() + heal);
      }
    }
  }

  @Override
  public void onDefend(final EntityDamageByEntityEvent event) {

  }

  @Override
  public void onDamaged(final EntityDamageEvent event) {
    final Player player = (Player) event.getEntity();
    final Environment environment = player.getLocation().getWorld().getEnvironment();
    if (environment == Environment.NETHER) {
      event.setDamage(event.getDamage() * 1.35);
    } else if (environment == Environment.THE_END) {
      event.setDamage(event.getDamage() * 1.10);
    }
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

  }

  @Override
  public void onLogin(final Player player) {

  }

  @Override
  public List<String> getDescription() {
    final List<String> desc = new ArrayList<>();

    desc.add("§7Diese Rasse ist die Spitze der");
    desc.add("§7menschlichen Spezies.");
    desc.add("");
    desc.add("§aRassenbonus I");
    desc.add("§e6% §7mehr Erfahrung für deiner Klasse.");
    desc.add("");
    desc.add("§aRassenbonus II");
    desc.add("§7Gekochtes Essen wie Fleisch oder Kuchen");
    desc.add("§estellen §e2 + 20% §7deines fehlenden Lebens");
    desc.add("§7wieder her.");
    desc.add("§7 - Schweine-, Kuh- Lachs-, und Schaafsteaks");
    desc.add("§7 - Suppen und Kürbiuskuchen");
    desc.add("");
    desc.add("§cRassenmalus");
    desc.add("§7Du erleidest §c35%§7 mehr Schaden im Nether");
    desc.add("§7und §c10% §7mehr Schaden im End.");
    desc.add("");
    desc.add("§9Spezialrezept");
    desc.add("§8Königlicher Banner");
    desc.add("§7Ein Banner, welcher von einer Person getragen");
    desc.add("§7werden kann. Diese Person erleidet doppelten");
    desc.add("§7Schaden. Alle Verbündeten in einem Umkreis von");
    desc.add("§e32 §7Blöcken verursachen §e33% §7mehr Schaden und");
    desc.add("§7sind §e10% §7schneller.");
    desc.add("§7Negiert den Malus von Menschen.");

    return desc;
  }

  @Override
  public Model getRaceLeaderCrownModel() {
    return Model.HUMAN_CROWN;
  }
}
