package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.destroystokyo.paper.MaterialTags;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.rolemodule.skills.BleedingDebuff;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.common.UtilMobs;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 21.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public enum RoleType {

  SWORD_FIGHTER("Schwertkämpfer", Model.SWORD_FIGHTER_ICON.getChar()) {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.SWORD_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Model.SWORD_FIGHTER_ICON.getItem()).name("§eSchwertkämpfer");

      builder.lore("", "§6Stufe 1 ->§f Scharfe Klingen", "§7Schwerter haben eine Chance von §e8%");
      builder.lore("§7eine Blutende Wunde zuzufügen,", "§7welche im Verlauf von §e5s", "§7insgesamt §e5 Schaden verursacht.");

      builder.lore("", "§6Stufe 10 ->§f Verfolgungsjagd", "§7Nach einem Treffer wird dein Lauftempo");
      builder.lore("§7für §e3s §7erhöht.");

      builder.lore("", "§6Stufe 20 ->§f Schwachstellen finden", "§7Die Chance auf eine Blutung wird");
      builder.lore("§7auf §e26.5% §7angehoben.", "§7Du verursachst zusätzlich §e25% §7mehr Schaden", "§7an blutenden Gegnern.");

      builder.lore("", "§6Rüstungen:", "§7Netherite: -2.5% Schaden pro Teil");
      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnteil in deiner Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      builder.flag(ItemFlag.HIDE_ATTRIBUTES);
      return builder.build();
    }

    @Override
    public boolean isSuitableWeapon(final ItemStack itemStack) {
      return MaterialTags.SWORDS.isTagged(itemStack);
    }

    @Override
    public void handleAttacking(final EntityDamageByEntityEvent event, final int lvl, final Player attacker, final LivingEntity defender) {
      if (lvl < 1) {
        return;
      }
      final double bloodChance = lvl > 19 ? 0.25 : 0.06;
      if (ThreadLocalRandom.current().nextDouble() < bloodChance) {
        new BleedingDebuff(defender).start();
      }
      if (lvl < 10) {
        return;
      }
      attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
      if (lvl < 20) {
        return;
      }
      if (BleedingDebuff.isAffected(defender)) {
        defender.getWorld().playSound(defender.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_FALL, 0.8F, 0.33F);
        event.setDamage(event.getDamage() * 1.33);
      }
    }

    @Override
    public double getDamageArmorScale(final ItemStack[] armor) {
      double scale = 1.0;
      for (final ItemStack itemStack : armor) {
        if (itemStack == null) {
          continue;
        }
        final Material material = itemStack.getType();
        if (RoleType.NETHERITE_ARMOR.contains(material)) {
          scale -= 0.025;
        }
      }
      return scale;
    }
  },

  AXE_FIGHTER("Axtkämpfer", Model.AXE_FIGHTER_ICON.getChar()) {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.AXE_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Model.AXE_FIGHTER_ICON.getItem()).name("§e" + this.displayName);

      builder.lore("", "§6Stufe 1 ->§f Kraftvolle Schläge", "§7Ein Angriff mit einer Axt verursacht");
      builder.lore("§7immer §eein Herz §7zusätzlichen Schaden,", "§7unabhängig von der Rüstung des Feindes.");

      builder.lore("", "§6Stufe 10 ->§f Präzises spalten", "§7Du hast eine Chance von §e16.5%");
      builder.lore("§7einen Präzisen Schlag zu landen,", "§7welcher §e50% §7mehr Schaden verursacht.");

      builder.lore("", "§6Stufe 20 ->§f Exekutieren", "§7Wenn du einen Gegner, welcher");
      builder.lore("§7weniger als §e15% §7seine Lebenspunkte", "§7besitzt angreifst, stirbt er sofort.");

      builder.lore("", "§6Rüstungen:", "§7Netherite: -2.5% Schaden pro Teil");
      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnteil in deiner Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      builder.flag(ItemFlag.HIDE_ATTRIBUTES);

      return builder.build();
    }

    @Override
    public boolean isSuitableWeapon(final ItemStack itemStack) {
      return MaterialTags.AXES.isTagged(itemStack);
    }

    @Override
    public void handleAttacking(final EntityDamageByEntityEvent event, final int lvl, final Player attacker, final LivingEntity defender) {
      if (lvl < 1) {
        return;
      }
      UtilMobs.trueDamage(defender, DamageCause.ENTITY_ATTACK, 2);
      if (lvl < 10) {
        return;
      }
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      if (random.nextDouble() < 0.2) {
        event.setDamage(event.getDamage() * 1.50);
      }
      if (lvl < 20) {
        return;
      }
      final double percentHealth = 1.0 / defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * defender.getHealth();
      if (percentHealth < 0.16) {
        final World world = defender.getWorld();
        world.spawnParticle(Particle.BLOCK_DUST, defender.getLocation().add(0, 0.5, 0), 16, 0.33, 0.33, 0.33,
            Material.BLACK_WOOL.createBlockData());
        world.playSound(defender.getLocation(), Sound.BLOCK_ANVIL_HIT, 1F, 0.33F);
        world.playSound(defender.getLocation(), Sound.ENTITY_PANDA_HURT, 0.8F, 0.25F);
        UtilMobs.trueDamage(defender, DamageCause.ENTITY_ATTACK, defender.getHealth());
      }
    }

    @Override
    public double getDamageArmorScale(final ItemStack[] armor) {
      double scale = 1.0;
      for (final ItemStack itemStack : armor) {
        if (itemStack == null) {
          continue;
        }
        final Material material = itemStack.getType();
        if (RoleType.NETHERITE_ARMOR.contains(material)) {
          scale -= 0.025;
        }
      }
      return scale;
    }

  },

  SPEAR_FIGHTER("Speerkämpfer", Model.SPEAR_FIGHTER_ICON.getChar()) {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.SPEAR_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Model.SPEAR_FIGHTER_ICON.getItem()).name("§e" + this.displayName);

      builder.lore("", "§6Stufe 1 ->§f Spitze Speere", "§7Speere haben eine Chance von §e13.5%");
      builder.lore("§7dich durch ihren verursachten,", "§7Schaden zu heilen.");

      builder.lore("", "§6Stufe 10 ->§f Kampfkunst", "§7Du hast eine Chance von §e10%");
      builder.lore("§7einen Angriff zu blocken.", "§7Speere verursachen §e1/2 Herz", "§7zusätzlichen Schaden.");

      builder.lore("", "§6Stufe 20 ->§f Rundumschlag", "§7Du kannst mit deinem Speer");
      builder.lore("§7um dich schlagen, um alle", "§7Gegner weg zu stoßen und", "§7insgesamt §e3.5 Herzen §7Schaden zu");
      builder.lore("§7verursachen. §e5 Sek §7Abklingzeit.");

      builder.lore("", "§6Rüstungen:", "§7Diamant: -2.5% Schaden pro Teil", "§7Netherite: -4% Schaden pro Teil");
      return RoleType.completeCreation(sum, current, builder);
    }

    @Override
    public boolean isSuitableWeapon(final ItemStack itemStack) {
      return RoleModule.isSpear(itemStack);
    }

    @Override
    public void handleAttacking(final EntityDamageByEntityEvent event, final int lvl, final Player attacker, final LivingEntity defender) {
      if (lvl < 1) {
        return;
      }
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      if (random.nextDouble() < 0.135) {
        final double dmg = event.getDamage();
        attacker.setHealth(Math.min(attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), attacker.getHealth() + dmg));
      }
      if (lvl < 10) {
        return;
      }
      UtilMobs.trueDamage(defender, DamageCause.ENTITY_ATTACK, 1);
    }

    @Override
    public double getDamageArmorScale(final ItemStack[] armor) {
      double scale = 1.0;
      for (final ItemStack itemStack : armor) {
        if (itemStack == null) {
          continue;
        }
        final Material material = itemStack.getType();
        if (RoleType.DIAMOND_ARMOR.contains(material)) {
          scale -= 0.025;
        } else if (RoleType.NETHERITE_ARMOR.contains(material)) {
          scale -= 0.04;
        }
      }
      return scale;
    }

  },

  BOW_FIGHTER("Schütze", Model.BOW_FIGHTER_ICON.getChar()) {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.BOW_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Model.BOW_FIGHTER_ICON.getItem()).name("§e" + this.displayName);

      builder.lore("", "§6Stufe 1 ->§f Spitze Pfeile", "§7Pfeile/Bolzen haben eine Chance von §e16%", "§7eine Blutende Wunde zuzufügen,");
      builder.lore("§7welche im Verlauf von §e5 Sekunden", "§7insgesamt §e5Schaden §7verursacht.");

      builder.lore("", "§6Stufe 10 ->§f Überspannen", "§7Pfeile/Bolzen sind §e33% §7schneller,", "§7und fliegen weiter.");

      builder.lore("", "§6Stufe 20 ->§f Harter Einschlag", "§7Pfeile/Bolzen verursachen zusätzlich");
      builder.lore("§e2Herzen §7Schaden und", "§7verlangsamen das Ziel kurz.");

      builder.lore("", "§6Rüstungen:", "§7Eisen: -4% Schaden pro Teil", "§7Diamant: -10% Schaden pro Teil");
      builder.lore("§7Netherite: -12.5% Schaden pro Teil");
      return RoleType.completeCreation(sum, current, builder);
    }

    @Override
    public boolean isSuitableWeapon(final ItemStack itemStack) {
      return MaterialTags.BOWS.isTagged(itemStack);
    }

    @Override
    public void handleAttacking(final EntityDamageByEntityEvent event, final int lvl, final Player attacker, final LivingEntity defender) {

    }

    @Override
    public double getDamageArmorScale(final ItemStack[] armor) {
      double scale = 1.0;
      for (final ItemStack itemStack : armor) {
        if (itemStack == null) {
          continue;
        }
        final Material material = itemStack.getType();
        if (RoleType.IRON_ARMOR.contains(material)) {
          scale -= 0.04;
        } else if (RoleType.DIAMOND_ARMOR.contains(material)) {
          scale -= 0.08;
        } else if (RoleType.NETHERITE_ARMOR.contains(material)) {
          scale -= 0.125;
        }
      }
      return scale;
    }

  },

  ALCHEMIST("Alchemist", Model.ALCHEMIST_ICON.getChar()) {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.ALCHEMIST);
      final ItemBuilder builder = new ItemBuilder(Model.ALCHEMIST_ICON.getItem()).name("§e" + this.displayName);

      builder.lore("", "§6Stufe 1 ->§f Glut", "§7Angriffe mit einem Alchemistenstab", "§7verursachen zufällig zwischen");
      builder.lore("§e1.5 §7und§e 3.5 Herzen §7Schaden, welcher", "§7nicht von Rüstungen verhindert", "§7werden kann.");

      builder.lore("", "§6Stufe 10 ->§f Tödliche Tränke", "§7Jeder geworfene Trank ver-", "§7ursacht §e1 §7bis §e2 Herzen §7Schaden,");
      builder.lore("§7welcher nicht verhindert", "§7werden kann.");

      builder.lore("", "§6Stufe 20 ->§f Feuer Magie", "§7Du kannst mit einem Alchemistenstab");
      builder.lore("§7Feuerbälle verschießen. Diese", "§ehaben eine Abklingzeit von", "§e3 Sekunden §7und verursachen");
      builder.lore("§7zwischen §e3 §7und §e5 Herzen §7Schaden,", "§7welcher nicht verhinder werden kann,", "§7aber Blaze Powder als");
      builder.lore("§7Munition benötigt.");

      builder.lore("", "§6Rüstungen:", "§7Eisen: -3% Schaden pro Teil", "§7Diamant: -8% Schaden pro Teil");
      builder.lore("§7Netherite: -10% Schaden pro Teil");
      return RoleType.completeCreation(sum, current, builder);
    }

    @Override
    public boolean isSuitableWeapon(final ItemStack itemStack) {
      return RoleModule.isAlchemistStaff(itemStack);
    }

    @Override
    public void handleAttacking(final EntityDamageByEntityEvent event, final int lvl, final Player attacker, final LivingEntity defender) {
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      UtilMobs.trueDamage(defender, DamageCause.MAGIC, random.nextInt(3, 8));
      defender.getWorld().spawnParticle(Particle.FLAME, defender.getLocation().add(0, 0.5, 0), 4, 0.15, 0.15, 0.15, 0.2);
    }

    @Override
    public double getDamageArmorScale(final ItemStack[] armor) {
      double scale = 1.0;
      for (final ItemStack itemStack : armor) {
        if (itemStack == null) {
          continue;
        }
        final Material material = itemStack.getType();
        if (RoleType.IRON_ARMOR.contains(material)) {
          scale -= 0.03;
        } else if (RoleType.DIAMOND_ARMOR.contains(material)) {
          scale -= 0.08;
        } else if (RoleType.NETHERITE_ARMOR.contains(material)) {
          scale -= 0.10;
        }
      }
      return scale;
    }

  },

  SHAMAN("Schamane", Model.SHAMAN_ICON.getChar()) {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.ALCHEMIST);
      final ItemBuilder builder = new ItemBuilder(Model.SHAMAN_ICON.getItem()).name("§e" + this.displayName);

      builder.lore("", "§6Stufe 1 ->§f Hex", "§7Angriffe mit einem Schamanenstab", "§7verursachen zufällig zwischen");
      builder.lore("§e1 §7und§e 2.5 Herzen §7Schaden, welcher", "§7nicht von Rüstungen verhindert", "§7werden kann.");
      builder.lore("§eAußerdem wirst du dabei um §e0.5", "§7Herzen geheilt.");

      builder.lore("", "§6Stufe 10 ->§f Tödliche Tränke", "§7Jeder geworfene Trank ver-", "§7ursacht §e1 §7bis §e2 Herzen §7Schaden,");
      builder.lore("§7welcher nicht verhindert", "§7werden kann.");

      builder.lore("", "§6Stufe 20 ->§f Hexen Magie", "§7Du kannst mit einem Schamanenstab");
      builder.lore("§7Verderbnis verschießen. Diese", "§ehat eine Abklingzeit von", "§e4 Sekunden §7und verursacht");
      builder.lore("§7zwischen §e2 §7und §e5 Herzen §7Schaden,", "§7welcher nicht verhinder werden kann,");
      builder.lore("§7und dich um §e1 Herz §7heilt.", "§7Es wird 2 Knochenmehl als", "§7Munition benötigt.");

      builder.lore("", "§6Rüstungen:", "§7Eisen: -3% Schaden pro Teil", "§7Diamant: -8% Schaden pro Teil");
      builder.lore("§7Netherite: -10% Schaden pro Teil");
      return RoleType.completeCreation(sum, current, builder);
    }

    @Override
    public boolean isSuitableWeapon(final ItemStack itemStack) {
      return RoleModule.isShamanStaff(itemStack);
    }

    @Override
    public void handleAttacking(final EntityDamageByEntityEvent event, final int lvl, final Player attacker, final LivingEntity defender) {
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      UtilMobs.trueDamage(defender, DamageCause.MAGIC, random.nextInt(3, 8));
      defender.getWorld().spawnParticle(Particle.SMOKE_NORMAL, defender.getLocation().add(0, 0.5, 0), 4, 0.15, 0.15, 0.15, 0.2);
      attacker.setHealth(Math.min(attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), attacker.getHealth() + 1));
    }

    @Override
    public double getDamageArmorScale(final ItemStack[] armor) {
      double scale = 1.0;
      for (final ItemStack itemStack : armor) {
        if (itemStack == null) {
          continue;
        }
        final Material material = itemStack.getType();
        if (RoleType.IRON_ARMOR.contains(material)) {
          scale -= 0.03;
        } else if (RoleType.DIAMOND_ARMOR.contains(material)) {
          scale -= 0.08;
        } else if (RoleType.NETHERITE_ARMOR.contains(material)) {
          scale -= 0.10;
        }
      }
      return scale;
    }

  };

  private static ItemStack completeCreation(final int sum, final int current, final ItemBuilder builder) {
    final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
    builder.lore("", "§fAnzahl in dieser Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
    builder.flag(ItemFlag.HIDE_ATTRIBUTES);
    return builder.build();
  }

  @Getter
  protected final String displayName;
  @Getter
  private final char chatIcon;

  private static final EnumSet<Material> IRON_ARMOR = EnumSet
      .of(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS);
  private static final EnumSet<Material> DIAMOND_ARMOR = EnumSet
      .of(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS);
  private static final EnumSet<Material> NETHERITE_ARMOR = EnumSet
      .of(Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS);

  public abstract ItemStack getIcon(final Player player);

  public abstract boolean isSuitableWeapon(final ItemStack itemStack);

  public abstract void handleAttacking(EntityDamageByEntityEvent event, int lvl, Player attacker, LivingEntity defender);

  public abstract double getDamageArmorScale(ItemStack[] armor);

}
