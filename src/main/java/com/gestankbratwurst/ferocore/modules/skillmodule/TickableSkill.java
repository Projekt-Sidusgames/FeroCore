package com.gestankbratwurst.ferocore.modules.skillmodule;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 17.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface TickableSkill {

  void tick();

  boolean isDone();

  default boolean tickAndIsDone() {
    this.tick();
    return this.isDone();
  }

}
