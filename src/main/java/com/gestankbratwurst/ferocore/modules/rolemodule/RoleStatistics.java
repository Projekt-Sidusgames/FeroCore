package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.gestankbratwurst.ferocore.util.Msg;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.commons.lang.mutable.MutableLong;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RoleStatistics {

  private final Map<RoleAttribute, MutableInt> roleAttributes;
  private final Map<RoleType, MutableLong> roleExperience;
  @Getter
  private RoleType currentRole = null;
  private int currentLevel = 1;
  private final UUID playerID;
  @Getter
  private int attributePoints;

  protected RoleStatistics() {
    this(null);
  }

  public int getRoleAttribute(final RoleAttribute attribute) {
    return this.roleAttributes.get(attribute).intValue();
  }

  private void resetRoleAttributes() {
    this.attributePoints = this.roleAttributes.values().stream().mapToInt(MutableInt::intValue).sum();
    this.roleAttributes.values().forEach(mutable -> mutable.setValue(0));
  }

  public RoleStatistics(final UUID playerID) {
    this.roleExperience = new HashMap<>();
    this.roleAttributes = new HashMap<>();
    for (final RoleType roleType : RoleType.values()) {
      this.roleExperience.put(roleType, new MutableLong());
    }
    for (final RoleAttribute roleAttribute : RoleAttribute.values()) {
      this.roleAttributes.put(roleAttribute, new MutableInt());
    }
    this.playerID = playerID;
  }

  public long getCurrentTotalExp() {
    if (!this.hasSelectedRole()) {
      return 0;
    }
    return this.roleExperience.get(this.currentRole).longValue();
  }

  public DisplayableExperience getDisplayableExperience() {
    final long[] deltas = this.getDeltaExperiences();
    return new DisplayableExperience(deltas[0], deltas[1]);
  }

  public long[] getDeltaExperiences() {
    final long[] experiences = new long[2];
    final long current = this.getCurrentTotalExp();
    experiences[0] = current - RoleExperienceLUT.getTotalExperienceToLevel(this.currentLevel);
    experiences[1] =
        RoleExperienceLUT.getTotalExperienceToLevel(this.currentLevel + 1) - RoleExperienceLUT.getTotalExperienceToLevel(this.currentLevel);
    return experiences;
  }

  public int getLevel() {
    return this.currentLevel;
  }

  private void recalculateLevel() {
    if (!this.hasSelectedRole()) {
      return;
    }
    this.currentLevel = RoleExperienceLUT.getLevelOf(this.roleExperience.get(this.currentRole).longValue());
  }

  public void setActiveRole(final RoleType roleType) {
    this.resetRoleAttributes();
    this.currentRole = roleType;
    this.recalculateLevel();
  }

  public boolean hasSelectedRole() {
    return this.currentRole != null;
  }

  public void addExperience(final long exp) {
    if (!this.hasSelectedRole()) {
      return;
    }
    this.roleExperience.get(this.currentRole).add(exp);
    this.checkForLevelUp();
  }

  public void removeExperience(final long exp) {
    if (!this.hasSelectedRole()) {
      return;
    }
    this.roleExperience.get(this.currentRole).subtract(exp);
    this.checkForLevelUp();
  }

  private void checkForLevelUp() {
    final int lvlups = RoleExperienceLUT.getLevelUps(this.currentLevel, this.roleExperience.get(this.currentRole).longValue());
    for (int i = 0; i < lvlups; i++) {
      this.levelUp();
    }
  }

  private void levelUp() {
    final Player player = Bukkit.getPlayer(this.playerID);
    this.attributePoints += 2;
    this.currentLevel++;
    if (player == null) {
      return;
    }
    Msg.send(player, "Klasse", "Du bist jetzt level §e" + this.currentLevel);
  }

}