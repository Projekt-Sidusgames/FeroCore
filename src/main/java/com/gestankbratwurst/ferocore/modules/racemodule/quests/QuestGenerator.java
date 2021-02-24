package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class QuestGenerator {

  private final Map<QuestType, Function<RaceType, Quest>> questTypeSupplierMap;

  public QuestGenerator() {
    this.questTypeSupplierMap = ImmutableMap.of(QuestType.KILL, this::generateKillQuest, QuestType.GATHER, this::generateGatherQuest);
  }

  public Quest generateQuest(final RaceType raceType) {
    final QuestType[] types = QuestType.values();
    return this.generateQuest(types[ThreadLocalRandom.current().nextInt(types.length)], raceType);
  }

  public Quest generateQuest(final QuestType questType, final RaceType raceType) {
    return this.questTypeSupplierMap.get(questType).apply(raceType);
  }

  private Quest generateKillQuest(final RaceType raceType) {
    return SimpleKillQuestGenerator.generateSimpleKillQuest(ThreadLocalRandom.current().nextInt(1, 4));
  }

  private Quest generateGatherQuest(final RaceType raceType) {
    return SimpleGatherQuestGenerator.generateSimpleKillQuest(ThreadLocalRandom.current().nextInt(1, 4), raceType);
  }

}