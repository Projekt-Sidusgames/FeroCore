package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.util.common.UtilItem;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class FeroPlayer {

  protected static transient PlayerModule PLAYER_MODULE;

  public static FeroPlayer of(final Player player) {
    return of(player.getUniqueId());
  }

  public static FeroPlayer of(final UUID playerID) {
    return PLAYER_MODULE.getFeroPlayer(playerID);
  }

  protected FeroPlayer() {
    this(null);
  }

  public FeroPlayer(final UUID playerID) {
    this.tags = new HashSet<>();
    this.loginRemoverTags = new HashMap<>();
    this.playerID = playerID;
  }

  @Setter
  @Getter
  private GameProfile lastSeenGameProfile;
  @Getter
  private final UUID playerID;
  @Getter
  private RaceType raceType;

  private final Set<String> tags;
  private final Map<String, Integer> loginRemoverTags;

  public void addTempTag(final String tag, final int ticks) {
    final Integer currentID = this.loginRemoverTags.get(tag);
    if (currentID != null) {
      Bukkit.getScheduler().cancelTask(currentID);
    }
    final int taskID = TaskManager.getInstance().runBukkitSyncDelayed(() -> this.removeTag(tag), ticks);
    this.loginRemoverTags.put(tag, taskID);
    this.tags.add(tag);
  }

  public void addTag(final String tag) {
    this.tags.add(tag);
  }

  public boolean hasTag(final String tag) {
    return this.tags.contains(tag);
  }

  public boolean removeTag(final String tag) {
    this.loginRemoverTags.remove(tag);
    return this.tags.remove(tag);
  }

  public ItemStack getLastSeenHead() {
    return this.lastSeenGameProfile == null ? null : UtilItem.getHeadFromGameProfile(this.lastSeenGameProfile);
  }

  public String getLastSeenName() {
    return this.lastSeenGameProfile.getName();
  }

  public void addToRace(final RaceType newType) {
    final Race newRace = newType.getRace();
    if (this.raceType != null) {
      this.getRace().removeMember(this.playerID);
    }
    newRace.addMember(this.playerID);
    this.raceType = newType;
  }

  public boolean hasChosenRace() {
    return this.raceType != null;
  }

  public Race getRace() {
    if (!this.hasChosenRace()) {
      return null;
    }
    return this.raceType.getRace();
  }

  protected void clearRemovalTags() {
    new ArrayList<>(this.loginRemoverTags.keySet()).forEach(this::removeTag);
  }

}
