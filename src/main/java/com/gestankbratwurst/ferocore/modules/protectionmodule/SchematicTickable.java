package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.protectionmodule.Schematic.RelativeBlockData;
import com.gestankbratwurst.ferocore.util.UtilModule;
import com.gestankbratwurst.ferocore.util.actionbar.ActionBarBoard;
import com.gestankbratwurst.ferocore.util.actionbar.ActionBarBoard.Section;
import com.gestankbratwurst.ferocore.util.actionbar.ActionBarManager;
import com.gestankbratwurst.ferocore.util.actionbar.ActionLine;
import com.gestankbratwurst.ferocore.util.common.UtilMath;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;
import org.bukkit.Location;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 20.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SchematicTickable implements LimitedSerializableTickable {

  private final Queue<RelativeBlockData> dataToPlace;
  private final Location relativeLocation;
  private final int blocksPerTick;
  private boolean done = false;
  private final int max;
  private int progress;
  private final UUID playerID;
  private final ActionLine line;

  public SchematicTickable(final Schematic schematic, final Location pasteLocation, final int blocksPerTick, final UUID playerID) {
    this.dataToPlace = new ArrayDeque<>();
    schematic.fill(this.dataToPlace);
    this.relativeLocation = pasteLocation;
    this.blocksPerTick = blocksPerTick;
    this.max = this.dataToPlace.size();
    this.progress = 0;
    this.playerID = playerID;
    if (playerID != null) {
      final ActionBarManager actionBarManager = FeroCore.getModule(UtilModule.class).getActionBarManager();
      final ActionBarBoard board = actionBarManager.getBoard(playerID);
      this.line = board.getSection(Section.MIDDLE).addLayer(5, this::showProgress);
    } else {
      this.line = null;
    }
  }

  @Override
  public void tick() {
    for (int i = 0; i < this.blocksPerTick; i++) {
      final RelativeBlockData placeData = this.dataToPlace.poll();
      if (placeData == null) {
        this.done = true;
        break;
      }
      if (!placeData.placeRelativeTo(this.relativeLocation)) {
        i--;
      }
      this.progress++;
    }
  }

  private String showProgress() {
    final double percent = ((int) (1000.0 / this.max * this.progress)) / 10.0;
    final String color;
    if (percent < 25) {
      color = "§c";
    } else if (percent < 50) {
      color = "§6";
    } else if (percent < 75) {
      color = "§e";
    } else {
      color = "§a";
    }
    return UtilMath.getPercentageBar(this.progress, this.max, 50, "|") + "§e [" + color + percent + "%" + "§e]";
  }

  @Override
  public boolean isDone() {
    if (this.done) {
      final ActionBarManager actionBarManager = FeroCore.getModule(UtilModule.class).getActionBarManager();
      final ActionBarBoard board = actionBarManager.getBoard(this.playerID);
      TaskManager.getInstance().runBukkitSyncDelayed(() -> board.getSection(Section.MIDDLE).removeLayer(this.line), 20);
    }
    return this.done;
  }
}
