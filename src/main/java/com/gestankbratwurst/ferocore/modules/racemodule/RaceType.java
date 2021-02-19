package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.racemodule.races.DwarfRace;
import com.gestankbratwurst.ferocore.modules.racemodule.races.ElfRace;
import com.gestankbratwurst.ferocore.modules.racemodule.races.HumanRace;
import com.gestankbratwurst.ferocore.modules.racemodule.races.OrcRace;
import com.gestankbratwurst.ferocore.modules.racemodule.races.UndeadRace;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum RaceType {

  HUMAN(new HumanRace("Kaiserliche", Model.HUMAN_ICON), HumanRace.class),
  DWARF(new DwarfRace("Zwerge", Model.DWARF_ICON), DwarfRace.class),
  ELF(new ElfRace("Elfen", Model.ELF_ICON), ElfRace.class),
  UNDEAD(new UndeadRace("Untote", Model.UNDEAD_ICON), UndeadRace.class),
  ORC(new OrcRace("Orks", Model.ORC_ICON), OrcRace.class);

  @Getter
  @Setter(AccessLevel.PACKAGE)
  private Race race;
  @Getter
  private final Class<? extends Race> raceClass;

  public static int getLowestMemberCount() {
    return Arrays.stream(RaceType.values())
        .map(RaceType::getRace)
        .mapToInt(Race::getMemberCount)
        .min()
        .getAsInt();
  }

  public static RaceType fromName(final String name) {
    for (final RaceType raceType : values()) {
      if (raceType.getRace().getDisplayName().equals(name)) {
        return raceType;
      }
    }
    return null;
  }

}
