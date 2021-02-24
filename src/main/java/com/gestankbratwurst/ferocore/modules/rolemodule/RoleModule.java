package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.util.UtilModule;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RoleModule implements BaseModule {

  @Override
  public void enable(final FeroCore plugin) {
    FeroCore.registerListener(new RoleListener(FeroCore.getModule(UtilModule.class).getHologramManager()));
  }

  @Override
  public void disable(final FeroCore plugin) {

  }
}
