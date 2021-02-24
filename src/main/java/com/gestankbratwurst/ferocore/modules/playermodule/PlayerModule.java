package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.modules.io.FeroIO;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import java.io.Flushable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PlayerModule implements BaseModule, Flushable {

  private static final long SECONDS_BETWEEN_DATA_FLUSH = 900;
  private final Map<UUID, FeroPlayer> feroPlayerMap = new HashMap<>();

  private FeroIO feroIO;

  @Override
  public void enable(final FeroCore plugin) {
    FeroCore.registerListener(new PlayerDataListener(this));
    FeroPlayer.PLAYER_MODULE = this;
    this.feroIO = plugin.getFeroIO();
    this.feroIO.loadAllFeroPlayers().forEach(this::addFeroPlayer);
    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::flush, SECONDS_BETWEEN_DATA_FLUSH * 20, SECONDS_BETWEEN_DATA_FLUSH * 20);
  }

  @Override
  public void disable(final FeroCore plugin) {
    this.feroPlayerMap.values().forEach(FeroPlayer::clearRemovalTags);
    this.flush();
  }

  @Override
  public void flush() {
    final ArrayList<FeroPlayer> playerList = new ArrayList<>(this.feroPlayerMap.values());
    this.feroIO.saveAllFeroPlayer(playerList);
  }

  public FeroPlayer getFeroPlayer(final UUID playerID) {
    return this.feroPlayerMap.get(playerID);
  }

  protected void initPlayer(final Player player) {
    final UUID playerID = player.getUniqueId();
    if (!this.feroPlayerMap.containsKey(playerID)) {
      this.addFeroPlayer(this.feroIO.loadFeroPlayer(playerID));
    }
    final FeroPlayer feroPlayer = this.getFeroPlayer(player.getUniqueId());
    TaskManager.getInstance().runBukkitSyncDelayed(feroPlayer::reapplyActionDisplay, 10L);
    TaskManager.getInstance().runBukkitSync(() -> feroPlayer.setLastSeenGameProfile(UtilPlayer.getGameProfile(player)));
  }

  private void addFeroPlayer(final FeroPlayer feroPlayer) {
    this.feroPlayerMap.put(feroPlayer.getPlayerID(), feroPlayer);
  }
}
