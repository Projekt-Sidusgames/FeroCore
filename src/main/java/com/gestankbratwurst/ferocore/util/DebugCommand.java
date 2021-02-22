package com.gestankbratwurst.ferocore.util;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Values;
import com.gestankbratwurst.ferocore.resourcepack.SkinChooserGUI;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.ferocore.util.common.UtilItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 25.10.2020
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
// TODO remove
@CommandAlias("ferodebug")
@CommandPermission("admin.debug")
public class DebugCommand extends BaseCommand {

  @Default
  public void onDefault(final CommandSender sender) {

  }

  @Subcommand("customsound")
  public void onCustomSound(final Player sender, final CustomSound sound) {
    sound.play(sender);
    Msg.send(sender, "Debug", "Spiele custom Sound: " + Msg.elem(sound.toString()));
  }

  @Subcommand("head fromname")
  public void onNamedHead(final Player sender, final String name) {
    sender.getInventory().addItem(UtilItem.getHeadFromName(name));
    Msg.send(sender, "Debug", "Du erhälst den Kopf von: " + Msg.elem(name));
  }

  @Subcommand("head frombase64")
  public void onBaseHead(final Player sender, final String name, final String data) {
    sender.getInventory().addItem(UtilItem.getHeadFromBase64(name, data));
    Msg.send(sender, "Debug", "Du erhälst den Kopf von: " + Msg.elem(name));
  }

  @Subcommand("models get")
  @CommandCompletion("@Models")
  public void onModelItem(final Player sender, @Values("@Models") final Model skin) {
    sender.getInventory().addItem(skin.getItem());
    Msg.send(sender, "Debug", "Du erhälst das Item zu: " + skin);
  }

  @Subcommand("skin")
  @CommandCompletion("@Skins")
  public void onSkin(final Player sender, @Values("@Skins") final Model skin) {
    skin.applySkinTo(sender);
    Msg.send(sender, "Skins", "Dein skin wurde geändert: §e" + skin.toString());
  }

  @Subcommand("head skin")
  @CommandCompletion("@Skins")
  public void onHeadSkin(final Player sender, @Values("@Skins") final Model skin) {
    sender.getInventory().addItem(skin.getHead());
    Msg.send(sender, "Skins", "Kopf bekommen: §e" + skin.toString());
  }

  @Subcommand("skinchooser")
  @CommandCompletion("@Skins")
  public void onSkinChoose(final Player sender) {
    SkinChooserGUI.open(sender);
    Msg.send(sender, "Skins", "Wähle einen Skin.");
  }

}
