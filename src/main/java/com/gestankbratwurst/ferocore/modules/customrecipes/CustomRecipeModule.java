package com.gestankbratwurst.ferocore.modules.customrecipes;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomRecipeModule implements BaseModule {

  @Getter
  private CustomRecipeManager customRecipeManager;

  @Override
  public void enable(final FeroCore plugin) {
    this.customRecipeManager = new CustomRecipeManager();
    FeroCore.registerListener(new CustomRecipeListener(this.customRecipeManager));
  }

  @Override
  public void disable(final FeroCore plugin) {

  }
}
