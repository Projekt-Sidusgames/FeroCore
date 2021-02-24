package com.gestankbratwurst.ferocore.modules.rolemodule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 24.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public enum RoleAttribute {

  DAMAGE("§eSchaden"),
  VITALITY("§eLebenskraft"),
  ACCURACY("§ePräzision"),
  DEFENCE("§eVerteidigung");

  @Getter
  private final String displayName;

}
