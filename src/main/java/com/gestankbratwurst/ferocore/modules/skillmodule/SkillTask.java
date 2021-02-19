package com.gestankbratwurst.ferocore.modules.skillmodule;

import java.util.LinkedList;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 17.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SkillTask implements Runnable {

  private final LinkedList<TickableSkill> tickableSkills = new LinkedList<>();

  public void addSkill(final TickableSkill tickableSkill) {
    this.tickableSkills.add(tickableSkill);
  }

  @Override
  public void run() {
    this.tickableSkills.removeIf(TickableSkill::tickAndIsDone);
  }

}
