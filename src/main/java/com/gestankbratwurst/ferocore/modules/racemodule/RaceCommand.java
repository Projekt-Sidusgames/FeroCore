package com.gestankbratwurst.ferocore.modules.racemodule;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.gestankbratwurst.ferocore.modules.racemodule.items.elf.ElfOrbHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.human.HolyBookHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.undead.UndeadTotemHandle;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
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
@RequiredArgsConstructor
@CommandAlias("race|rasse")
public class RaceCommand extends BaseCommand {

  private final RaceModule raceModule;

  @Default
  public void onDefault(final Player player) {
    RaceMainGUI.open(player);
  }

  @Subcommand("admin declare war")
  @CommandPermission("admin")
  @CommandCompletion("@RaceType @RaceType")
  public void onAdminWarDeclare(final Player player, final RaceType from, final RaceType to) {
    this.raceModule.declareWar(from, to);
  }

  @Subcommand("admin msg")
  @CommandPermission("admin")
  @CommandCompletion("@RaceType")
  public void onAdminMsg(final Player player, final RaceType target, final String message) {
    final ChatColor color = target.getRace().getNameChatColor();
    target.getRace().sendMessage(color + "[§cADM" + color + "] " + player.getName(), message);
  }

  @Subcommand("admin openchooser")
  @CommandPermission("admin")
  public void onAdminOpenChooser(final Player player) {
    RaceChooserGUI.open(player);
  }

  @Subcommand("admin getcrown")
  @CommandPermission("admin")
  @CommandCompletion("@RaceType")
  public void onAdminCrown(final Player player, final RaceType target) {
    player.getInventory().addItem(target.getRace().createLeaderCrown());
  }

  @Subcommand("admin declare peace")
  @CommandPermission("admin")
  @CommandCompletion("@RaceType @RaceType")
  public void onAdminPeaceDeclare(final Player player, final RaceType from, final RaceType to) {
    this.raceModule.declarePeace(from, to);
  }

  @Subcommand("admin elforb")
  @CommandPermission("admin")
  public void onOrb(final Player player) {
    final ItemStack item = ElfOrbHandle.createOrb(1);
    player.getInventory().addItem(item);
  }

  @Subcommand("admin undeadtotem")
  @CommandPermission("admin")
  public void onUndeadTotem(final Player player) {
    final ItemStack item = UndeadTotemHandle.createTotem();
    player.getInventory().addItem(item);
  }

  @Subcommand("admin holybook")
  @CommandPermission("admin")
  public void onHolyBook(final Player player) {
    final ItemStack item = HolyBookHandle.createBook();
    player.getInventory().addItem(item);
  }

}
