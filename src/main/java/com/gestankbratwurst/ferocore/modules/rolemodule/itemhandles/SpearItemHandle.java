package com.gestankbratwurst.ferocore.modules.rolemodule.itemhandles;

import com.gestankbratwurst.ferocore.modules.customitems.CustomItemHandle;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleType;
import com.gestankbratwurst.ferocore.modules.rolemodule.skills.SpearSkill;
import com.gestankbratwurst.ferocore.modules.skillmodule.SkillModule;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 27.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SpearItemHandle implements CustomItemHandle {

  @Override
  public String getKey() {
    return "SPEAR";
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
    if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
      return;
    }
    final Player player = event.getPlayer();
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRole() || feroPlayer.getChosenRoleTye() != RoleType.SPEAR_FIGHTER) {
      return;
    }
    if (feroPlayer.getRoleLevel() < 20) {
      return;
    }
    final long cd = SkillModule.getCooldownLeft(player, "SPEAR_SKILL");
    if (cd > 0) {
      return;
    }
    SkillModule.setCooldown(player, "SPEAR_SKILL", 1000);
    SpearSkill.cast(player);
  }

  @Override
  public void handleBeingClicked(final InventoryClickEvent event, final ItemStack item) {

  }

}
