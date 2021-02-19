package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@EqualsAndHashCode
public abstract class Race {

  @Getter
  private final String displayName;
  @Getter
  private final Model icon;

  @Setter
  @Getter
  private ChatColor nameChatColor = ChatColor.YELLOW;
  @Getter
  protected final RaceRuleSet defaultRuleSet = new RaceRuleSet();

  protected final Set<RaceType> atWarSet = new HashSet<>();
  protected final Set<RaceType> peaceRequests = new HashSet<>();

  protected final Set<UUID> members = new HashSet<>();
  protected final Set<UUID> onlineMembers = new HashSet<>();
  protected final Map<UUID, RaceRuleSet> memberRules = new HashMap<>();

  public Race(final String displayName, final Model icon) {
    this.displayName = displayName;
    this.icon = icon;
  }

  public void removePeaceRequest(final RaceType otherRace) {
    this.peaceRequests.remove(otherRace);
  }

  public void addPeaceRequestFrom(final RaceType otherRace) {
    this.peaceRequests.add(otherRace);
  }

  public boolean hasPeaceRequestFrom(final RaceType raceType) {
    return this.peaceRequests.contains(raceType);
  }

  public boolean isAtWarWith(final RaceType raceType) {
    return this.atWarSet.contains(raceType);
  }

  public void addWarEnemy(final RaceType raceType) {
    this.atWarSet.add(raceType);
  }

  public RaceRuleState getEffectiveRuleState(final UUID playerID, final RaceRuleType rule) {
    final RaceRuleSet ruleSet = this.getRulesOf(playerID);
    final RaceRuleState state = ruleSet.getState(rule);
    if (state == RaceRuleState.DEFAULT) {
      final RaceRuleState defaultState = this.defaultRuleSet.getState(rule);
      return defaultState == RaceRuleState.DEFAULT ? RaceRuleState.ALLOW : defaultState;
    }
    return state;
  }

  public void removeWarEnemy(final RaceType raceType) {
    this.atWarSet.remove(raceType);
    this.peaceRequests.remove(raceType);
  }

  public boolean isMember(final UUID playerID) {
    return this.members.contains(playerID);
  }

  public boolean isEnemy(final UUID playerID) {
    for (final RaceType raceType : this.atWarSet) {
      if (raceType.getRace().isMember(playerID)) {
        return true;
      }
    }
    return false;
  }

  public void forEachRaceEntry(final BiConsumer<UUID, RaceRuleSet> entryConsumer) {
    this.memberRules.forEach(entryConsumer);
  }

  public RaceRuleSet getRulesOf(final UUID playerID) {
    return this.memberRules.get(playerID);
  }

  public ItemStack getInfoIcon() {
    final ItemBuilder builder = new ItemBuilder(this.icon.getItem());

    builder.name("§e" + this.displayName);
    builder.lore(this.getDescription());
    builder.lore("");

    final int memberCount = this.getMemberCount();
    final int diff = memberCount - RaceType.getLowestMemberCount();

    builder.amount(Math.max(1, memberCount));

    String color = "§a";
    String sub = "§7Hier ist noch viel Platz.";
    if (diff > 3) {
      color = "§6";
      sub = "§7Diese Rasse ist leicht übervölkert.";
    }
    if (diff >= 5) {
      color = "§c";
      sub = "§cDiese Rasse ist übervölkert. Kein Beitritt möglich.";
    }

    builder.lore("§fMitglieder: " + color + this.getMemberCount());
    builder.lore(sub);

    return builder.build();
  }

  public boolean isCrowned(final Player player) {
    return this.isCrownOfRace(player.getInventory().getHelmet());
  }

  public boolean isCrownOfRace(final ItemStack itemStack) {
    if (itemStack == null) {
      return false;
    }
    final ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      return false;
    }
    final PersistentDataContainer pdc = meta.getPersistentDataContainer();
    final NamespacedKey key = NameSpaceFactory.provide("RACE_CROWN");
    final String raceString = pdc.get(key, PersistentDataType.STRING);
    if (raceString == null) {
      return false;
    }

    return raceString.equals(this.getDisplayName());
  }

  public ItemStack createLeaderCrown() {
    final ItemBuilder builder = new ItemBuilder(this.getRaceLeaderCrownModel().getItem()).name("§eKrone der " + this.getDisplayName());

    builder.addPersistentData("RACE_CROWN", PersistentDataType.STRING, this.getDisplayName());
    builder.lore("");
    builder.lore("§7Mach das Rassenmenü auf, während");
    builder.lore("§7du diese Krone in der Hand hast.");
    builder.lore("§7Klicke auf das Kronensymbol, um");
    builder.lore("§7dich zu krönen.");

    return builder.build();
  }

  public void onChat(final Player who, final String msg) {
    final String out = this.getNameChatColor() + who.getName() + "§f [" + this.getIcon().getChar() + "] >> §7" + msg;
    who.getLocation().getNearbyPlayers(80).forEach(pl -> {
      if (!this.members.contains(pl.getUniqueId())) {
        pl.sendMessage("§f!" + out);
      }
    });
    this.forEachOnlineMember(pl -> pl.sendMessage(out));
  }

  public void sendMessage(final String prefix, final String msg) {
    final String out = prefix + "§f [" + this.getIcon().getChar() + "] >> §7" + msg;
    this.forEachOnlineMember(pl -> pl.sendMessage(out));
  }

  public void forEachOnlineMember(final Consumer<Player> playerConsumer) {
    this.onlineMembers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(playerConsumer);
  }

  public void forEachMemberAccount(final Consumer<FeroPlayer> feroPlayerConsumer) {
    this.members.stream().map(FeroPlayer::of).forEach(feroPlayerConsumer);
  }

  public void setAsOnline(final UUID memberID) {
    if (!this.members.contains(memberID)) {
      throw new UnsupportedOperationException();
    }
    this.onlineMembers.add(memberID);
  }

  public void setAsOffline(final UUID memberID) {
    if (!this.members.contains(memberID)) {
      throw new UnsupportedOperationException();
    }
    this.onlineMembers.remove(memberID);
  }

  public void addMember(final UUID playerID) {
    this.members.add(playerID);
    this.memberRules.put(playerID, new RaceRuleSet());
    final Player player = Bukkit.getPlayer(playerID);
    if (player != null) {
      this.setAsOnline(playerID);
    }
  }

  public void removeMember(final UUID playerID) {
    this.members.remove(playerID);
    this.memberRules.remove(playerID);
    this.onlineMembers.remove(playerID);
  }

  public int getMemberCount() {
    return this.members.size();
  }

  public abstract void init(JavaPlugin javaPlugin);

  public abstract void onSecond(Player player);

  public abstract void onAttack(EntityDamageByEntityEvent event);

  public abstract void onBlockBreak(BlockBreakEvent event);

  public abstract void onBlockDrop(BlockDropItemEvent event);

  public abstract void onConsume(PlayerItemConsumeEvent event);

  public abstract void onDefend(EntityDamageByEntityEvent event);

  public abstract void onDamaged(EntityDamageEvent event);

  public abstract void onShoot(ProjectileLaunchEvent event);

  public abstract void onPotionEffect(EntityPotionEffectEvent event);

  public abstract void onInteract(PlayerInteractAtEntityEvent event);

  public abstract boolean canEquip(Player who, ItemStack item);

  public abstract void onLogout(Player player);

  public abstract void onLogin(Player player);

  public abstract List<String> getDescription();

  public abstract Model getRaceLeaderCrownModel();

}