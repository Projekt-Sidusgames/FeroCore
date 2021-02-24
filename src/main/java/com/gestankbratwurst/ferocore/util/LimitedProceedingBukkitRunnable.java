package com.gestankbratwurst.ferocore.util;

import java.util.Collection;
import java.util.LinkedList;
import org.bukkit.scheduler.BukkitRunnable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class LimitedProceedingBukkitRunnable extends BukkitRunnable {

  private final LinkedList<Proceedable> proceedableQueue;

  public LimitedProceedingBukkitRunnable(final Collection<Proceedable> proceedables) {
    this.proceedableQueue = new LinkedList<>(proceedables);
  }

  @Override
  public void run() {
    this.proceedableQueue.removeIf(Proceedable::proceed);
    if (this.proceedableQueue.isEmpty()) {
      this.cancel();
    }
  }
}
