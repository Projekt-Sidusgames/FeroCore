package com.gestankbratwurst.ferocore.modules.protectionmodule;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Values;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.util.Msg;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 20.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
@CommandAlias("prot|p")
@CommandPermission("protection")
public class ProtectionCommand extends BaseCommand {

  private final ProtectionModule protectionModule;
  private final SchematicManager schematicManager;

  @Default
  public void onDefault(final Player player) {
    Msg.send(player, "Protection", "§e/p pos1");
    Msg.send(player, "Protection", "§e/p pos2");
  }

  @Subcommand("pos1")
  public void onPosOne(final Player player) {
    FeroPlayer.of(player).getProtectionSession().setFirstLoc(player.getLocation());
  }

  @Subcommand("pos2")
  public void onPosTwo(final Player player) {
    FeroPlayer.of(player).getProtectionSession().setSecondLoc(player.getLocation());
  }

  @Subcommand("clear positions")
  public void onPosOneClear(final Player player) {
    FeroPlayer.of(player).getProtectionSession().dropLocations();
  }

  @Subcommand("clear box")
  public void onBoxClear(final Player player) {
    FeroPlayer.of(player).getProtectionSession().clearBox();
  }

  @Subcommand("clear all")
  public void onAllClear(final Player player) {
    FeroPlayer.of(player).getProtectionSession().dropLocations();
    FeroPlayer.of(player).getProtectionSession().clearBox();
  }

  @Subcommand("create raceregion")
  @CommandCompletion("@RaceType")
  public void onCreate(final Player player, @Values("@RaceType") final RaceType raceType, final int priority) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    final BoundingBox boundingBox = feroPlayer.getProtectionSession().getBoundingBox();
    if (boundingBox == null) {
      Msg.send(player, "Protection", "Du musst erst eine Auswahl haben.");
      return;
    }
    this.protectionModule.createRegion(boundingBox, player.getWorld(), raceType, priority);
  }

  @Subcommand("schematic create")
  public void onSchematicCreate(final Player player, final String name) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    final ProtectionSession protectionSession = feroPlayer.getProtectionSession();
    final BoundingBox boundingBox = feroPlayer.getProtectionSession().getBoundingBox();
    if (boundingBox == null) {
      Msg.send(player, "Protection", "Du musst erst eine Auswahl haben.");
      return;
    }
    Msg.send(player, "Schematic", "Schematic wird async erstell...");
    this.schematicManager.createSchematicAsync(player.getLocation(), boundingBox, name).thenAccept(schematic -> {
      Msg.send(player, "Schematic", "Schematic wurde erfolgreich erstellt.");
    });
  }

  @Subcommand("schematic paste")
  @CommandCompletion("@Schematics")
  public void onSchematicPaste(final Player player, @Values("@Schematics") final String name, final double blocksPerSecond) {
    final int bpt = (int) (blocksPerSecond / 20.0);
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    Msg.send(player, "Schematic", "Starte kopieren von schematic...");
    this.schematicManager.getSchematic(name).pasteAt(player.getLocation(), bpt, player);
  }

}
