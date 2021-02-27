package com.gestankbratwurst.ferocore.modules.racemodule.races;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeManager;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeModule;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.common.UtilMobs;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
public class OrcRace extends Race {

  private transient NamespacedKey rawMeatKey;
  private transient Set<Material> rawMeat = EnumSet.of(
      Material.BEEF,
      Material.MUTTON,
      Material.PORKCHOP,
      Material.RABBIT,
      Material.CHICKEN
  );

  public OrcRace(final String displayName, final Model icon) {
    super(displayName, icon);
  }

  private void setUnderRawMeatInfluence(final Player player) {
    player.getPersistentDataContainer().set(this.rawMeatKey, PersistentDataType.INTEGER, 5);
  }

  private int getAndDecrementRawMeatTicks(final Player player) {
    final PersistentDataContainer container = player.getPersistentDataContainer();
    if (!container.has(this.rawMeatKey, PersistentDataType.INTEGER)) {
      return 0;
    }
    final int decremented = container.get(this.rawMeatKey, PersistentDataType.INTEGER) - 1;
    if (decremented == 0) {
      container.remove(this.rawMeatKey);
    } else {
      container.set(this.rawMeatKey, PersistentDataType.INTEGER, decremented);
    }
    return decremented + 1;
  }

  @Override
  public void init(final JavaPlugin javaPlugin) {
    this.rawMeatKey = NameSpaceFactory.provide("RAW_MEAT_INFLUENCE");
    this.rawMeat = EnumSet.of(
        Material.BEEF,
        Material.MUTTON,
        Material.PORKCHOP,
        Material.RABBIT,
        Material.CHICKEN
    );
  }

  @Override
  public void onSecond(final Player player) {
    if (this.getAndDecrementRawMeatTicks(player) > 0) {
      final double hpMax = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
      final double hpAdd = hpMax * (0.2 / 5);
      player.setHealth(Math.min(player.getHealth() + hpAdd, hpMax));
    }
    if (ThreadLocalRandom.current().nextDouble() < 0.025) {
      player.setFoodLevel(player.getFoodLevel() - 1);
    }
  }

  @Override
  public void onAttack(final EntityDamageByEntityEvent event) {
    final Entity entity = event.getEntity();
    if (event.isCancelled() || entity instanceof Projectile) {
      return;
    }
    if (entity instanceof LivingEntity) {
      final boolean extra = FeroPlayer.of(event.getDamager().getUniqueId()).hasTag("ORC_HORN");
      final int extraDmg = extra ? 3 : 1;
      final LivingEntity livingEntity = (LivingEntity) entity;
      UtilMobs.trueDamage(livingEntity, DamageCause.ENTITY_ATTACK, extraDmg);
      if (extra) {
        livingEntity.getWorld()
            .spawnParticle(Particle.REDSTONE, livingEntity.getLocation(), 6, 0.5, 1.5, 0.5, new DustOptions(Color.RED, 1F));
      }
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
    final Material material = event.getItem().getType();
    if (this.rawMeat.contains(material)) {
      this.setUnderRawMeatInfluence(event.getPlayer());
    }
  }

  @Override
  public void onDefend(final EntityDamageByEntityEvent event) {

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

  }

  @Override
  public void onLogin(final Player player) {

  }

  @Override
  public List<String> getDescription() {
    final List<String> desc = new ArrayList<>();

    desc.add("§7Diese Rasse ist stumpf und brutal.");
    desc.add("");
    desc.add("§aRassenbonus I");
    desc.add("§7Nahkampfangriffe verursachen immer §eeinen");
    desc.add("§7zusätzlichen absoluten Schaden.");
    desc.add("");
    desc.add("§aRassenbonus II");
    desc.add("§7Rohes Fleisch stellt über §e5s§7 bis zu");
    desc.add("§e20% §7deines maximalen Lebens wieder her.");
    desc.add("");
    desc.add("§cRassenmalus");
    desc.add("§7Du verlierst mehr Essen.");
    desc.add("");
    desc.add("§9Spezialrezept");
    desc.add("§8Kriegshorn");
    desc.add("§7Kann verwendet werden, um einen kurzen Ansturm");
    desc.add("§7zu tätigen. Alle Orks in der Nähe bekommen für");
    desc.add("§e8 §7Sekunden §e30% §7mehr Lauftempo und der");
    desc.add("§7Rassenbonus I wird währenddessen verdreifacht.");

    return desc;
  }

  @Override
  public Model getRaceLeaderCrownModel() {
    return Model.UNDEAD_CROWN;
  }

  @Override
  public List<Model> listChoosableSkins() {
    if (this.choosableSkins == null) {
      this.choosableSkins = ImmutableList.of(
          Model.ORC_SKIN_1,
          Model.ORC_SKIN_2,
          Model.ORC_SKIN_3,
          Model.ORC_SKIN_4,
          Model.ORC_SKIN_5,
          Model.ORC_SKIN_6,
          Model.ORC_SKIN_7,
          Model.ORC_SKIN_8,
          Model.ORC_SKIN_9,
          Model.ORC_SKIN_10
      );
    }
    return this.choosableSkins;
  }

  @Override
  public List<CustomShapedRecipe> listRaceRecipes() {
    final List<CustomShapedRecipe> recipeList = new ArrayList<>();
    final CustomRecipeManager manager = FeroCore.getModule(CustomRecipeModule.class).getCustomRecipeManager();
    recipeList.add(manager.getRecipeForKey("orc_horn"));
    return recipeList;
  }

  @Override
  public List<RoleType> getChoosableRoles() {
    return Arrays.asList(RoleType.SWORD_FIGHTER, RoleType.AXE_FIGHTER, RoleType.BOW_FIGHTER, RoleType.SHAMAN);
  }

  @Override
  public void playThemeSound(final Player player) {
    CustomSound.ORC_THEME.play(player);
  }
}
