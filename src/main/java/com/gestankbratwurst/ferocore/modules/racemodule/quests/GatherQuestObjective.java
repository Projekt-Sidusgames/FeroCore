package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import java.util.List;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Item;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class GatherQuestObjective implements Predicate<Item> {

  @Getter
  private final int targetAmount;
  @Getter
  private final int rewardPoints;
  @Getter
  @Setter
  private Predicate<Item> condition = (item) -> false;
  @Getter
  @Setter
  private String display;
  @Getter
  @Setter
  private List<String> description;

  @Override
  public boolean test(final Item item) {
    return this.condition.test(item);
  }
}
