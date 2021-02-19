package com.gestankbratwurst.ferocore.modules;

import com.gestankbratwurst.ferocore.FeroCore;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.10.2020
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface BaseModule {

  void enable(FeroCore plugin);

  void disable(FeroCore plugin);

}