package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.playermodule.PlayerOptionType;
import lombok.RequiredArgsConstructor;
import net.crytec.libs.protocol.holograms.AbstractHologram;
import net.crytec.libs.protocol.holograms.impl.HologramManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RoleListener implements Listener {

  private static final Vector DMG_VECTOR = new Vector(0, 0.15, 0);
  private final HologramManager hologramManager;

  @EventHandler(priority = EventPriority.HIGH)
  public void onDamage(final EntityDamageEvent event) {
    if (event.isCancelled()) {
      return;
    }
    final Location baseLoc = event.getEntity().getLocation().add(0, 0.5, 0);
    final double dmg = (int) (event.getDamage() * 10.0) / 10.0;
    final AbstractHologram hologram = this.hologramManager
        .createHologram(baseLoc, pl -> FeroPlayer.of(pl).getPlayerOptions().isEnabled(PlayerOptionType.SHOW_DMG_HOLOGRAMS));
    hologram.appendTextLine("§6-" + dmg);
    this.hologramManager.decorateAsMoving(hologram, DMG_VECTOR, 30);
  }


  @EventHandler
  public void onExp(final PlayerPickupExperienceEvent event) {
    FeroPlayer.of(event.getPlayer()).addRoleExp(event.getExperienceOrb().getExperience());
  }

}
