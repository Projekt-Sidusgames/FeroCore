package com.gestankbratwurst.ferocore.modules.protectionmodule;

import java.util.Iterator;
import java.util.LinkedList;
import lombok.AccessLevel;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 20.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SchematicRunnable implements Runnable {

  @Getter(AccessLevel.PROTECTED)
  private static final SchematicRunnable instance = new SchematicRunnable();

  private final LinkedList<LimitedSerializableTickable> tickables;

  private SchematicRunnable() {
    this.tickables = new LinkedList<>();
  }

  public void addTickable(final LimitedSerializableTickable tickable) {
    this.tickables.add(tickable);
  }

  @Override
  public void run() {
    final Iterator<LimitedSerializableTickable> tickableIterator = this.tickables.iterator();
    while (tickableIterator.hasNext()) {
      final LimitedSerializableTickable tickable = tickableIterator.next();
      tickable.tick();
      if (tickable.isDone()) {
        tickableIterator.remove();
      }
    }
  }
}
