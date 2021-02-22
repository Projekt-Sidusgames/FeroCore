package com.gestankbratwurst.ferocore.modules.protectionmodule;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 20.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface LimitedSerializableTickable {

  // TODO make serializable

  void tick();

  boolean isDone();

}
