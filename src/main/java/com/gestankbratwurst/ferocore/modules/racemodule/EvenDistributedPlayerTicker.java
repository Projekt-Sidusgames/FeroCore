package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
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
public class EvenDistributedPlayerTicker implements Runnable {

  protected static EvenDistributedPlayerTicker start(final JavaPlugin plugin) {
    final EvenDistributedPlayerTicker ticker = new EvenDistributedPlayerTicker();
    Bukkit.getScheduler().runTaskTimer(plugin, ticker, 1L, 1L);
    return ticker;
  }

  private static final int DISTRIBUTION = 20;

  private EvenDistributedPlayerTicker() {
    this.playerPositionMap = new HashMap<>();
    this.uuidMatrix = new ArrayList<>();
    for (int i = 0; i < DISTRIBUTION; i++) {
      this.uuidMatrix.add(new ArrayList<>());
    }
  }

  private final Map<UUID, Integer> playerPositionMap;
  private final List<List<UUID>> uuidMatrix;
  private int currentPosition = 0;

  public void add(final UUID playerID) {
    int index = 0;
    List<UUID> current = this.uuidMatrix.get(index);
    for (int x = 0; x < DISTRIBUTION; x++) {
      final List<UUID> next = this.uuidMatrix.get(x);
      final int size = next.size();
      if (size == 0 || size < current.size()) {
        current = next;
        index = x;
      }
    }
    current.add(playerID);
    this.playerPositionMap.put(playerID, index);
  }

  public void remove(final UUID playerID) {
    this.uuidMatrix.get(this.playerPositionMap.get(playerID)).remove(playerID);
  }

  private void proceedPosition() {
    if (++this.currentPosition == DISTRIBUTION) {
      this.currentPosition = 0;
    }
  }

  @Override
  public void run() {
    for (final UUID uuid : this.uuidMatrix.get(this.currentPosition)) {
      final FeroPlayer fp = FeroPlayer.of(uuid);

      if (fp.hasChosenRace()) {
        fp.getRace().onSecond(Bukkit.getPlayer(uuid));
      }
      fp.getProtectionSession().tick();
      fp.checkTemporaryBossBars();
      fp.updateAndShowActionBar();
    }
    this.proceedPosition();
  }
}
