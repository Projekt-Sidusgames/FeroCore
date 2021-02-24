package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.playermodule.PlayerOptionGUI;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.Quest;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleChooserGUI;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.ferocore.util.Msg;
import com.gestankbratwurst.ferocore.util.common.ChatInput;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.CustomHeads;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 04.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class RaceMainGUI implements InventoryProvider {

  public static void open(final Player player) {
    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (!feroPlayer.hasChosenRace()) {
      RaceChooserGUI.open(player);
    } else {
      final Race race = feroPlayer.getRace();
      final char raceIcon = race.getIcon().getChar();
      SmartInventory.builder()
          .size(5)
          .provider(new RaceMainGUI(race))
          .title("Hauptmenü der " + race.getDisplayName() + " §f" + raceIcon)
          .build()
          .open(player);
    }
  }

  public static ClickableItem getBackToMainIcon() {
    final ItemStack icon = new ItemBuilder(Model.DOUBLE_GRAY_ARROW_LEFT.getItem()).name("§cZurück").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceMainGUI.open(player);
    });
  }

  private final Race race;

  @Override
  public void init(final Player player, final InventoryContent content) {

    content.set(SlotPos.of(1, 1), this.getMemberListIcon());
    content.set(SlotPos.of(1, 3), this.getQuestsIcon());
    content.set(SlotPos.of(1, 5), this.getDiplomacyIcon());
    content.set(SlotPos.of(1, 7), this.getBroadcastIcon());
    content.set(SlotPos.of(3, 1), this.getSkinChooserIcon(player));
    content.set(SlotPos.of(3, 3), this.getRecipeIcon());

    content.set(SlotPos.of(3, 7), this.getOptionsIcon());

    final FeroPlayer feroPlayer = FeroPlayer.of(player);
    if (feroPlayer.hasChosenRole()) {
      content.set(SlotPos.of(3, 5), this.getRoleStatsGUI(player));
    } else {
      content.set(SlotPos.of(3, 5), this.getRoleChooserIcon());
    }

    final ItemStack headItem = player.getInventory().getHelmet();
    if (this.race.isCrownOfRace(headItem)) {
      content.set(SlotPos.of(4, 4), this.getRaceLeaderIcon());
    } else {
      final ItemStack handItem = player.getInventory().getItemInMainHand();
      if (this.race.isCrownOfRace(handItem)) {
        content.set(SlotPos.of(4, 4), this.getCrowningIcon());
      }
    }
  }


  private ClickableItem getCrowningIcon() {
    final ItemStack icon = new ItemBuilder(this.race.getRaceLeaderCrownModel().getItem()).name("§eKröne dich").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      final ItemStack handItem = player.getInventory().getItemInMainHand();
      final ItemStack headItem = player.getInventory().getHelmet();
      if (this.race.isCrownOfRace(handItem)) {
        if (headItem != null && headItem.getType() != Material.AIR) {
          player.getWorld().dropItemNaturally(player.getLocation(), headItem);
        }
        player.getInventory().setHelmet(handItem);
        player.getInventory().setItemInMainHand(null);
        this.race.forEachOnlineMember(online -> {
          final String msg = "§e" + player.getName() + " wurde als König ausgrufen.";
          UtilPlayer.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.8F, 1.2F);
          online.sendMessage(msg);
          this.reopen(player);
        });
      }
    });
  }

  private ClickableItem getRoleStatsGUI(final Player player) {
    final ItemStack icon = new ItemBuilder(Model.ROLE_ICON.getItem()).name("§c- TODO -").build();
    return ClickableItem.of(icon, event -> {
    });
  }

  private ClickableItem getRoleChooserIcon() {
    final ItemStack icon = new ItemBuilder(Model.ROLE_ICON.getItem()).name("§eKlasse wählen").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RoleChooserGUI.open(player);
    });
  }

  private ClickableItem getSkinChooserIcon(final Player player) {
    final ItemStack icon = new ItemBuilder(this.race.getSkinOf(player).getHead()).name("§eSkin wählen").build();
    return ClickableItem.of(icon, event -> {
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceSkinChooserGUI.open(player);
    });
  }

  private ClickableItem getRecipeIcon() {
    final ItemStack icon = new ItemBuilder(Material.CRAFTING_TABLE).name("§eRassen Rezepte").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceRecipeSelectionGUI.open(player);
    });
  }

  private ClickableItem getBroadcastIcon() {
    final ItemStack icon = new ItemBuilder(Model.HORN_ICON.getItem())
        .name("§eKundgebung")
        .lore("")
        .lore("§7Schickt allen online Mitgliedern")
        .lore("§7eine wichtige Nachricht.")
        .build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();

      if (this.race.getEffectiveRuleState(player.getUniqueId(), RaceRuleType.BROADCAST) == RaceRuleState.DENY) {
        UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 0.8F, 0.8F);
        Msg.send(player, "Rasse", "Dir wurde diese Aktion verboten.");
        return;
      }

      player.closeInventory();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      Msg.send(player, "Rasse", "Schreibe dein Nachricht:");
      ChatInput.create(player, in -> this.race.forEachOnlineMember(online -> {
        online.sendTitle("", in, 10, 45, 10);
        Msg.send(online, "Rasse", "§fKundgebung von " + player.getName() + ":");
        Msg.send(online, "Rasse", in);
        CustomSound.TRUMPET.play(online);
      }));
    });
  }

  private ClickableItem getOptionsIcon() {
    final ItemStack icon = new ItemBuilder(CustomHeads.GLOBE.getItem()).name("§eDeine Optionen").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      PlayerOptionGUI.open(player);
    });
  }

  private ClickableItem getRaceLeaderIcon() {
    final ItemStack icon = new ItemBuilder(this.race.getIcon().getItem()).name("§eAnführermenü").build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceLeaderGUI.open(player);
    });
  }

  private ClickableItem getDiplomacyIcon() {
    final Model iconModel = this.race.atWarSet.isEmpty() ? Model.PEACE_ICON : Model.WAR_ICON;
    final ItemBuilder builder = new ItemBuilder(iconModel.getItem()).name("§eDiplomatie");
    builder.lore("");
    for (final RaceType raceType : RaceType.values()) {
      if (raceType.getRace().equals(this.race)) {
        continue;
      }
      final boolean atWar = this.race.atWarSet.contains(raceType);
      final char raceChar = raceType.getRace().getIcon().getChar();
      final String state = atWar ? "§cKrieg §f" + Model.WAR_ICON.getChar() : "§aFrieden §f" + Model.PEACE_ICON.getChar();
      builder.lore(" §f" + raceChar + " " + raceType.getRace().getDisplayName() + " >> " + state);
    }

    return ClickableItem.empty(builder.build());
  }

  private ClickableItem getMemberListIcon() {
    final ItemStack icon = new ItemBuilder(Model.BROWN_BOOK.getItem())
        .name("§eMitgliederliste: §f" + this.race.getMemberCount())
        .lore("")
        .lore("§7Klicke für Auflistung")
        .build();
    return ClickableItem.of(icon, event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceMemberListGUI.open(player);
    });
  }

  private ClickableItem getQuestsIcon() {
    final ItemBuilder icon = new ItemBuilder(Model.LETTER_NO.getItem())
        .name("§eQuests")
        .lore("");

    final List<Quest> quests = this.race.getActiveQuests();
    if (quests.isEmpty()) {
      icon.lore("§7Es gibt momentan keine Quests.");
    } else {
      for (final Quest quest : quests) {
        icon.lore("§f- " + quest.getName() + " für §e" + quest.getRewardPoints() + "§f Punkte [§e" + quest.getProgressPercent() + "%§f]");
      }
    }

    return ClickableItem.of(icon.build(), event -> {
      final Player player = (Player) event.getWhoClicked();
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      RaceQuestGUI.open(player);
    });
  }

}
