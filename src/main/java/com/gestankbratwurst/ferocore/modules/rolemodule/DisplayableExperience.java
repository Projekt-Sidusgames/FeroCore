package com.gestankbratwurst.ferocore.modules.rolemodule;

import lombok.Data;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 24.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
public class DisplayableExperience {

  private final long current;
  private final long next;

  public double getPercent() {
    return 1.0 / this.next * this.current;
  }

  public double getDisplayPercent() {
    return (int) (1000.0 / this.next * this.current) / 10.0;
  }

}