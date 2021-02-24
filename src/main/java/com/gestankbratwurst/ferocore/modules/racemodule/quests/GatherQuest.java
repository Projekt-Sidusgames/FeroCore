package com.gestankbratwurst.ferocore.modules.racemodule.quests;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.util.UtilModule;
import com.gestankbratwurst.ferocore.util.common.UtilMath;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.crytec.libs.protocol.holograms.MovingHologram;
import net.crytec.libs.protocol.holograms.impl.HologramManager;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class GatherQuest implements Quest {

  private final Map<GatherQuestObjective, MutableInt> progressionMap;
  private transient int targetSumCache = -1;
  private final transient BossBar progressBar;

  public GatherQuest() {
    this.progressionMap = new LinkedHashMap<>();
    this.progressBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_10);
    this.progressBar.setVisible(true);
  }

  public void addObjective(final GatherQuestObjective objective) {
    this.progressionMap.put(objective, new MutableInt());
  }

  private int getProgressSum() {
    return this.progressionMap.values().stream().mapToInt(MutableInt::intValue).sum();
  }

  private int getTargetSum() {
    if (this.targetSumCache == -1) {
      this.targetSumCache = this.progressionMap.keySet().stream().mapToInt(GatherQuestObjective::getTargetAmount).sum();
    }
    return this.targetSumCache;
  }

  public QuestProgressionResponse addIfValid(final Item item) {
    boolean valid = false;
    boolean completed = false;
    boolean progression = false;

    for (final Entry<GatherQuestObjective, MutableInt> entry : this.progressionMap.entrySet()) {
      valid = true;
      if (entry.getKey().test(item)) {
        final MutableInt progressionInt = entry.getValue();
        progressionInt.add(item.getItemStack().getAmount());
        final int target = entry.getKey().getTargetAmount();
        if (progressionInt.intValue() > target) {
          progressionInt.setValue(target);
        } else {
          progression = true;
          completed = this.getTargetSum() == this.getProgressSum();
          final HologramManager hologramManager = FeroCore.getModule(UtilModule.class).getHologramManager();
          final MovingHologram hologram = hologramManager.createTemporaryHologram(item.getLocation(), 20);
          hologram.getHologram().appendTextLine("§e✦");
        }
        this.progressBar.setProgress(this.getProgressPercent() / 100.0);
        this.progressBar.setTitle("§f" + this.getName() + " §e[" + this.getProgressPercent() + "%]");
        break;
      }
    }

    if (valid) {
      if (completed) {
        return QuestProgressionResponse.COMPLETE;
      } else if (progression) {
        return QuestProgressionResponse.PROGRESS;
      } else {
        return QuestProgressionResponse.NO_PROGRESS;
      }
    } else {
      return QuestProgressionResponse.NONE;
    }
  }

  @Override
  public double getProgressPercent() {
    return (int) (1000.0 / this.getTargetSum() * this.getProgressSum()) / 10.0;
  }

  @Override
  public String getName() {
    return "Sammle Items";
  }

  @Override
  public List<String> getDescription() {
    final List<String> desc = new ArrayList<>();

    for (final Entry<GatherQuestObjective, MutableInt> entry : this.progressionMap.entrySet()) {
      final GatherQuestObjective objective = entry.getKey();
      final int progression = entry.getValue().intValue();
      desc.add("§f- " + objective.getDisplay() + " [" + progression + "/" + objective.getTargetAmount() + "]");
      for (final String line : objective.getDescription()) {
        desc.add("§7   " + line);
      }
    }

    desc.add("");
    desc.add(UtilMath.getPercentageBar(this.getProgressSum(), this.getTargetSum(), 40, "▮") + " §e[" + this.getProgressPercent() + "%]");

    return desc;
  }

  @Override
  public ItemStack getIcon() {
    final ItemStack icon = new ItemBuilder(Model.GATHER_QUEST_ICON.getItem())
        .name("§e" + this.getName() + " [§6Punkte: " + this.getRewardPoints() + "§e]")
        .lore(this.getDescription())
        .build();

    final Damageable damageable = (Damageable) icon.getItemMeta();
    damageable.setDamage((int) (icon.getType().getMaxDurability() * (1.0 - this.getProgressPercent() / 100.0)));
    icon.setItemMeta((ItemMeta) damageable);

    return icon;
  }

  @Override
  public int getRewardPoints() {
    return this.progressionMap.keySet().stream().mapToInt(GatherQuestObjective::getRewardPoints).sum();
  }

  @Override
  public void addProgressBarView(final Player player) {
    FeroPlayer.of(player).addOrRefreshTemporaryBossBar(this.progressBar, 3000);
  }

}
