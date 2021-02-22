package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 21.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public enum RoleType {

  SWORD_FIGHTER("Schwertkämpfer") {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.SWORD_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Material.IRON_SWORD).name("§eSchwertkämpfer");

      builder.lore("", "§fStufe 1 -> Scharfe Klingen", "§7Schwerter haben eine Chance von §e8%");
      builder.lore("§7eine Blutende Wunde zuzufügen,", "§7welche im Verlauf von §e5s", "§7insgesamt §e5 Schaden verursacht.");

      builder.lore("", "§fStufe 10 -> Verfolgungsjagd", "§7Nach einem Treffer wird dein Lauftempo");
      builder.lore("§7für §e3s §7erhöht.");

      builder.lore("", "§fStufe 20 -> Schwachstellen finden", "§7Die Chance auf eine Blutung wird");
      builder.lore("§7auf §e26.5% §7angehoben.", "§7Du verursachst zusätzlich §e25% §7mehr Schaden", "§7an blutenden Gegnern.");

      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnteil in deiner Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      return builder.build();
    }
  },
  AXE_FIGHTER("Axtkämpfer") {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.AXE_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Material.IRON_SWORD).name("§e" + this.displayName);

      builder.lore("", "§fStufe 1 -> Kraftvolle Schläge", "§7Ein Angriff mit einer Axt verursacht");
      builder.lore("§7immer §eein Herz §7zusätzlichen Schaden,", "§7unabhängig von der Rüstung des Feindes.");

      builder.lore("", "§fStufe 10 -> Präzises spalten", "§7Deine Chance auf kritische Treffer");
      builder.lore("§7ist um §e16.5%§7 erhöht.");

      builder.lore("", "§fStufe 20 -> Exekutieren", "§7Wenn du einen Gegner, welcher");
      builder.lore("§7weniger als §e15% §7seine Lebenspunkte", "§7besitzt angreifst, stirbt er sofort.");
      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnteil in deiner Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      return builder.build();
    }
  },
  SPEAR_FIGHTER("Speerkämpfer") {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.SPEAR_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Material.IRON_SHOVEL).name("§e" + this.displayName);

      builder.lore("", "§fStufe 1 -> Spitze Speere", "§7Speere haben eine Chance von §e13.5%");
      builder.lore("§7dich durch ihren verursachten,", "§7Schaden zu heilen.");

      builder.lore("", "§fStufe 10 -> Kampfkunst", "§7Du hast eine Chance von §e10%");
      builder.lore("§7einen Angriff zu blocken.", "Speere verursachen §e1/2 Herz", "§7zusätzlichen Schaden.");

      builder.lore("", "§fStufe 20 -> Pikenier", "§7Deine Angriffe haben zusätzlich");
      builder.lore("§e50% §7mehr Reichweite und", "§7verlangsamen kurz.");
      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnzahl in dieser Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      return builder.build();
    }
  },
  BOW_FIGHTER("Schütze") {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.BOW_FIGHTER);
      final ItemBuilder builder = new ItemBuilder(Material.BOW).name("§e" + this.displayName);

      builder.lore("", "§fStufe 1 -> Spitze Pfeile", "§7Pfeile/Bolzen haben eine Chance von §e16%", "§7eine Blutende Wunde zuzufügen,");
      builder.lore("§7welche im Verlauf von §e5 Sekunden", "§7insgesamt §e5Schaden §7verursacht.");

      builder.lore("", "§fStufe 10 -> Überspannen", "§7Pfeile/Bolzen sind §e33% §7schneller,", "§7und fliegen weiter.");

      builder.lore("", "§fStufe 20 -> Harter Einschlag", "§7Pfeile/Bolzen verursachen zusätzlich");
      builder.lore("§e2 Herzen §7Schaden und", "§7verlangsamen das Ziel kurz.");
      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnzahl in dieser Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      return builder.build();
    }
  },
  ALCHEMIST("Alchemist") {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.ALCHEMIST);
      final ItemBuilder builder = new ItemBuilder(Material.BLAZE_ROD).name("§e" + this.displayName);

      builder.lore("", "§fStufe 1 -> Glut", "§7Angriffe mit einem Feuerstab", "§7verursachen zufällig zwischen");
      builder.lore("§e2 §7und§e 5 Herzen §7Schaden, welcher", "§7nicht von Rüstungen verhindert", "§7werden kann.");

      builder.lore("", "§fStufe 10 -> Tödliche Tränke", "§7Jeder geworfene Trank ver-", "§7ursacht §e1 §7oder §e2 §7Schaden,");
      builder.lore("§7welcher nicht verhindert", "§7werden kann.");

      builder.lore("", "§fStufe 20 -> Feuer Magie", "§7Du kannst mit einem Feuerstab");
      builder.lore("§7Feuerbälle verschießen. Diese", "§ehaben eine Abklingzeit von", "§e3 Sekunden §7und verursachen");
      builder.lore("§7zwischen §e3 §7und §e5 Herzen §7Schaden,", "§7welcher nicht verhinder werden kann,", "§7aber Blaze Powder als");
      builder.lore("§7Munition benötigt.");
      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnzahl in dieser Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      return builder.build();
    }
  },
  SHAMAN("Schamane") {
    @Override
    public ItemStack getIcon(final Player player) {
      final FeroPlayer feroPlayer = FeroPlayer.of(player);
      final int sum = feroPlayer.getRace().getTotalRolesChosen();
      final int current = feroPlayer.getRace().getAmountOfRole(RoleType.ALCHEMIST);
      final ItemBuilder builder = new ItemBuilder(Material.BLAZE_ROD).name("§e" + this.displayName);

      builder.lore("", "§fStufe 1 -> Hex", "§7Angriffe mit einem Schamanenstab", "§7verursachen zufällig zwischen");
      builder.lore("§e2 §7und§e 5 Herzen §7Schaden, welcher", "§7nicht von Rüstungen verhindert", "§7werden kann.");

      builder.lore("", "§fStufe 10 -> Tödliche Tränke", "§7Jeder geworfene Trank ver-", "§7ursacht §e1 §7oder §e2 §7Schaden,");
      builder.lore("§7welcher nicht verhindert", "§7werden kann.");

      builder.lore("", "§fStufe 20 -> Hexen Magie", "§7Du kannst mit einem Schamanenstab");
      builder.lore("§7Verderbnis verschießen. Diese", "§ehat eine Abklingzeit von", "§e4 Sekunden §7und verursacht");
      builder.lore("§7zwischen §e2 §7und §e5 Herzen §7Schaden,", "§7welcher nicht verhinder werden kann,");
      builder.lore("§7und dich um §e1 Herz §7heilt.", "§7Es wird 2 Knochenmehl als", "§7Munition benötigt.");
      final double percent = sum == 0 ? 0.0 : ((int) (1000.0 / sum * current)) / 10.0;
      builder.lore("", "§fAnzahl in dieser Rasse: §e" + current + "/" + sum + " [" + percent + "%]");
      return builder.build();
    }
  };

  protected final String displayName;

  public abstract ItemStack getIcon(final Player player);

}
