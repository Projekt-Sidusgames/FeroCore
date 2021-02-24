package com.gestankbratwurst.ferocore.modules.playermodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.protectionmodule.ProtectionSession;
import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.modules.rolemodule.DisplayableExperience;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleStatistics;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleType;
import com.gestankbratwurst.ferocore.util.UtilModule;
import com.gestankbratwurst.ferocore.util.actionbar.ActionBarManager;
import com.gestankbratwurst.ferocore.util.common.UtilItem;
import com.gestankbratwurst.ferocore.util.common.UtilMath;
import com.gestankbratwurst.ferocore.util.tasks.TaskManager;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
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
    this.protectionSession = new ProtectionSession(playerID);
    this.temporaryBossBars = new HashMap<>();
    this.playerOptions = new PlayerOptions();
    this.roleStatistics = new RoleStatistics(playerID);
    this.chosenActionDisplays = new ChosenActionDisplay[3];
    this.actionBarManager = FeroCore.getModule(UtilModule.class).getActionBarManager();
  }

  @Setter
  @Getter
  private GameProfile lastSeenGameProfile;
  @Getter
  private final UUID playerID;
  @Getter
  private RaceType raceType;
  @Getter
  private final ProtectionSession protectionSession;
  @Getter
  private final PlayerOptions playerOptions;
  private final RoleStatistics roleStatistics;

  private final Set<String> tags;
  private final Map<String, Integer> loginRemoverTags;
  private final transient Map<BossBar, Long> temporaryBossBars;
  private final transient ActionBarManager actionBarManager;

  private final ChosenActionDisplay[] chosenActionDisplays;

  protected void setChosenActionDisplay(final PlayerActionPosition position, final PlayerActionBarType actionBarType) {
    final ChosenActionDisplay actionDisplay = new ChosenActionDisplay(actionBarType, position);
    this.getOnlinePlayer().ifPresent(actionDisplay::apply);
    this.chosenActionDisplays[position.getSection().getIndex()] = actionDisplay;
  }

  protected void reapplyActionDisplay() {
    System.out.println("RE_TEST");
    this.getOnlinePlayer().ifPresent(online -> {
      System.out.println("APPLY");
      for (final ChosenActionDisplay display : this.chosenActionDisplays) {
        if (display != null) {
          display.apply(online);
        }
      }
    });
  }

  public void changeRole(final RoleType roleType) {
    this.roleStatistics.setActiveRole(roleType);
    this.setChosenActionDisplay(PlayerActionPosition.MIDDLE, PlayerActionBarType.LEVEL_DISPLAY_NUMBERS);
  }

  public void updateAndShowActionBar() {
    this.getOnlinePlayer().ifPresent(this.actionBarManager::updateAndShow);
  }

  public String getRoleLevelStringPercent() {
    if (!this.hasChosenRole()) {
      return "§cKeine Klasse";
    }
    final DisplayableExperience display = this.roleStatistics.getDisplayableExperience();

    return "§eLvl: §f" + this.roleStatistics.getLevel() + " "
        + UtilMath.getPercentageBar(display.getCurrent(), display.getNext(), 33, "|")
        + " §e[" + display.getDisplayPercent() + "%]";
  }

  public void addRoleExp(final long exp) {
    this.roleStatistics.addExperience(exp);
    this.updateAndShowActionBar();
  }

  public String getRoleLevelStringNumber() {
    if (!this.hasChosenRole()) {
      return "§cKeine Klasse";
    }
    final DisplayableExperience display = this.roleStatistics.getDisplayableExperience();

    return "§eLvl: §f" + this.roleStatistics.getLevel() + " "
        + UtilMath.getPercentageBar(display.getCurrent(), display.getNext(), 33, "|")
        + " §e[§f" + display.getCurrent() + "§e/§f" + display.getNext() + "§e]";
  }

  public void addOrRefreshTemporaryBossBar(final BossBar bossBar, final int millisToShow) {
    this.getOnlinePlayer().ifPresent(player -> {
      bossBar.addPlayer(player);
      this.temporaryBossBars.put(bossBar, System.currentTimeMillis() + millisToShow);
    });
  }

  public void checkTemporaryBossBars() {
    final long now = System.currentTimeMillis();
    final Set<BossBar> removers = new HashSet<>();
    for (final Entry<BossBar, Long> entry : this.temporaryBossBars.entrySet()) {
      final Optional<Player> optionalPlayer = this.getOnlinePlayer();
      if (now >= entry.getValue() && optionalPlayer.isPresent()) {
        entry.getKey().removePlayer(optionalPlayer.get());
        removers.add(entry.getKey());
      }
    }
    removers.forEach(this.temporaryBossBars::remove);
  }

  protected void clearFromTemporaryBossBars() {
    final Optional<Player> optionalPlayer = this.getOnlinePlayer();
    for (final BossBar bar : this.temporaryBossBars.keySet()) {
      optionalPlayer.ifPresent(bar::removePlayer);
    }
  }

  public boolean hasChosenRole() {
    return this.roleStatistics.hasSelectedRole();
  }

  public RoleType getChosenRoleTye() {
    return this.roleStatistics.getCurrentRole();
  }

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

  public Optional<Player> getOnlinePlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.playerID));
  }
}
