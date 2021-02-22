package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.gestankbratwurst.ferocore.util.common.UtilVect;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 20.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ProtectionSession {

  private final UUID playerID;
  transient private Location firstLoc;
  transient private Location secondLoc;
  private BoundingBox boundingBox;

  public ProtectionSession(final UUID playerID) {
    this.playerID = playerID;
  }

  public BoundingBox getBoundingBox() {
    if (this.boundingBox == null) {
      return null;
    }
    return this.boundingBox.clone();
  }

  public void clearBox() {
    this.boundingBox = null;
  }

  public void dropLocations() {
    this.firstLoc = null;
    this.secondLoc = null;
    this.boundingBox = null;
  }

  public void tick() {
    final Player player = Bukkit.getPlayer(this.playerID);
    if (this.boundingBox != null && player != null) {
      UtilVect.showBoundingBox(this.boundingBox, player, 32);
    }
  }

  public void setFirstLoc(final Location loc) {
    this.firstLoc = loc;
    this.evaluateBoundingBox();
  }

  public void setSecondLoc(final Location loc) {
    this.secondLoc = loc;
    this.evaluateBoundingBox();
  }

  private void evaluateBoundingBox() {
    if (this.firstLoc == null || this.secondLoc == null) {
      return;
    }
    this.boundingBox = BoundingBox.of(this.firstLoc.getBlock(), this.secondLoc.getBlock());
  }

}
