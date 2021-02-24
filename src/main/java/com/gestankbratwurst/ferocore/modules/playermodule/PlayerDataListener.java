package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.resourcepack.ResourcepackModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private final PlayerModule playerModule;

  @EventHandler
  public void onPreLogin(final AsyncPlayerPreLoginEvent event) {
    if (!ResourcepackModule.isServerRunning()) {
      event.disallow(Result.KICK_FULL, "Der Resourcepack Server startet noch.");
      event.setLoginResult(Result.KICK_FULL);
      return;
    }
    // TODO check for registration
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onLogin(final PlayerLoginEvent event) {
    final Player player = event.getPlayer();
    this.playerModule.initPlayer(player);
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    final FeroPlayer feroPlayer = this.playerModule.getFeroPlayer(event.getPlayer().getUniqueId());
    feroPlayer.clearFromTemporaryBossBars();

  }

}