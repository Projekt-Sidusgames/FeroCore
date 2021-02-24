package com.gestankbratwurst.ferocore.modules.racemodule.races;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeManager;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeModule;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomShapedRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleType;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.EntityType;
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
public class DwarfRace extends Race {

  private transient String caveModTag = "DW_CAVE";
  private transient String depModTag = "DW_DEPTH";
  private transient AttributeModifier caveMod;
  private transient AttributeModifier depthMod;
  private transient Set<Material> oreMaterials;

  public DwarfRace(final String displayName, final Model icon) {
    super(displayName, icon);

  }

  @Override
  public void init(final JavaPlugin javaPlugin) {
    this.caveModTag = "DW_CAVE";
    this.depModTag = "DW_DEPTH";
    this.oreMaterials = EnumSet.of(
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.LAPIS_ORE,
        Material.GOLD_ORE,
        Material.REDSTONE_ORE,
        Material.DIAMOND_ORE,
        Material.EMERALD_ORE,
        Material.NETHER_QUARTZ_ORE,
        Material.NETHER_GOLD_ORE
    );
    this.caveMod = new AttributeModifier(UUID.randomUUID(), this.caveModTag, 0.16, Operation.MULTIPLY_SCALAR_1);
    this.depthMod = new AttributeModifier(UUID.randomUUID(), this.depModTag, 0.1, Operation.MULTIPLY_SCALAR_1);
  }

  @Override
  public void onSecond(final Player player) {
    final Set<String> tags = player.getScoreboardTags();

    final AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    if (speedAttr == null) {
      return;
    }

    if (UtilPlayer.isInCave(player)) {
      if (tags.contains(this.depModTag)) {
        tags.remove(this.depModTag);
        speedAttr.removeModifier(this.depthMod);
      }
      if (!tags.contains(this.caveModTag)) {
        speedAttr.addModifier(this.caveMod);
        tags.add(this.caveModTag);
      }
    } else if (player.getLocation().getY() <= 58) {
      if (tags.contains(this.caveModTag)) {
        tags.remove(this.caveModTag);
        speedAttr.removeModifier(this.caveMod);
      }
      if (!tags.contains(this.depModTag)) {
        speedAttr.addModifier(this.depthMod);
        tags.add(this.depModTag);
      }
    } else if (tags.contains(this.caveModTag) || tags.contains(this.depModTag)) {
      tags.remove(this.depModTag);
      tags.remove(this.caveModTag);
      speedAttr.removeModifier(this.caveMod);
      speedAttr.removeModifier(this.depthMod);
    }
  }

  @Override
  public void onAttack(final EntityDamageByEntityEvent event) {

  }

  @Override
  public void onBlockBreak(final BlockBreakEvent event) {

  }

  @Override
  public void onBlockDrop(final BlockDropItemEvent event) {
    final Material mat = event.getBlockState().getType();
    if (!this.oreMaterials.contains(mat)) {
      return;
    }
    if (ThreadLocalRandom.current().nextDouble() <= 0.1) {
      final World world = event.getBlockState().getWorld();
      world.spawnParticle(Particle.VILLAGER_HAPPY, event.getBlockState().getLocation().add(.5, .5, .5), 4, .4, .4, .4);
      event.getItems().forEach(item -> world.dropItemNaturally(item.getLocation(), item.getItemStack().clone()));
    }
  }

  @Override
  public void onConsume(final PlayerItemConsumeEvent event) {

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
    if (event.getRightClicked().getType() == EntityType.HORSE) {
      event.setCancelled(true);
      Msg.send(event.getPlayer(), "Rasse", "Du traust dich nicht an das Pferd...");
    }
  }

  @Override
  public boolean canEquip(final Player who, final ItemStack item) {
    if (item.getType() == Material.BOW) {
      Msg.send(who, "Rasse", "Dieses komische Ding fällt dir aus der Hand.");
      return false;
    }
    return true;
  }

  @Override
  public void onLogout(final Player player) {
    final AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    if (speedAttr != null) {
      speedAttr.removeModifier(this.caveMod);
      speedAttr.removeModifier(this.depthMod);
      player.getScoreboardTags().remove(this.caveModTag);
      player.getScoreboardTags().remove(this.depModTag);
    }
  }

  @Override
  public void onLogin(final Player player) {

  }

  @Override
  public List<String> getDescription() {
    final List<String> desc = new ArrayList<>();

    desc.add("§7Diese Rasse spezielisiert sich");
    desc.add("§7auf das Leben unter der Erden.");
    desc.add("");
    desc.add("§aRassenbonus I");
    desc.add("§e10% §7Lauftempo unter 58 Blöcken und");
    desc.add("§e16% §7Lauftempo in Höhlen");
    desc.add("");
    desc.add("§aRassenbonus II");
    desc.add("§7Chance von §e10% §7beim abbauen von");
    desc.add("§7jeglichen Erzen §edoppelte§7 Drops zu erhalten");
    desc.add("");
    desc.add("§cRassenmalus");
    desc.add("§7Können keine Fernkampfwaffen verwenden");
    desc.add("§7und nicht reiten.");
    desc.add("");
    desc.add("§9Spezialrezept");
    desc.add("§8Zwergenkanone");
    desc.add("§7Kann als Waffe oder zum bergbau ein");
    desc.add("§7gesetzt werden.");

    return desc;
  }

  @Override
  public Model getRaceLeaderCrownModel() {
    return Model.DWARF_CROWN;
  }

  @Override
  public List<Model> listChoosableSkins() {
    if (this.choosableSkins == null) {
      this.choosableSkins = ImmutableList.of(
          Model.DWARF_SKIN_1,
          Model.DWARF_SKIN_2,
          Model.DWARF_SKIN_3,
          Model.DWARF_SKIN_4,
          Model.DWARF_SKIN_5,
          Model.DWARF_SKIN_6,
          Model.DWARF_SKIN_7,
          Model.DWARF_SKIN_8,
          Model.DWARF_SKIN_9,
          Model.DWARF_SKIN_10
      );
    }
    return this.choosableSkins;
  }

  @Override
  public List<CustomShapedRecipe> listRaceRecipes() {
    final List<CustomShapedRecipe> recipeList = new ArrayList<>();
    final CustomRecipeManager manager = FeroCore.getModule(CustomRecipeModule.class).getCustomRecipeManager();
    recipeList.add(manager.getRecipeForKey("canon_ball"));
    recipeList.add(manager.getRecipeForKey("dwarf_canon"));
    return recipeList;
  }

  @Override
  public List<RoleType> getChoosableRoles() {
    return Arrays.asList(RoleType.SWORD_FIGHTER, RoleType.AXE_FIGHTER, RoleType.ALCHEMIST);
  }

  @Override
  public void playThemeSound(final Player player) {
    CustomSound.DWARF_THEME.play(player);
  }
}
