package com.gestankbratwurst.ferocore.modules.racemodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemManager;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeManager;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeModule;
import com.gestankbratwurst.ferocore.modules.io.FeroIO;
import com.gestankbratwurst.ferocore.modules.racemodule.items.dwarf.CanonBallRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.items.dwarf.DwarfCanonHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.dwarf.DwarfCanonRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.items.elf.ElfOrbHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.elf.ElfOrbRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.items.human.CementItemHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.human.CementRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.items.human.HolyBookHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.human.HolyBookRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.items.orc.OrcHornItemHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.orc.OrcHornRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.items.undead.UndeadTotemHandle;
import com.gestankbratwurst.ferocore.modules.racemodule.items.undead.UndeadTotemRecipe;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.GatherQuestObjective;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.KillQuestObjective;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.Quest;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.json.GatherQuestObjectSerializer;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.json.KillQuestObjectiveSerializer;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.json.MutableIntSerializer;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.json.QuestSerializer;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.json.GsonProvider;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RaceModule implements BaseModule {

  public void declareWar(final RaceType from, final RaceType to) {
    final Race fromRace = from.getRace();
    final Race toRace = to.getRace();
    fromRace.addWarEnemy(to);
    toRace.addWarEnemy(from);
    final String top = fromRace.getIcon().getChar() + "    " + Model.WAR_ICON.getChar() + "    " + toRace.getIcon().getChar();
    final String bottom = "Ein Krieg wurde ausgerufen: §e" + fromRace.getDisplayName() + " §cgegen§e " + toRace.getDisplayName();
    UtilPlayer.forEachOnlineDistributed(15, (online) -> {
      online.sendTitle(top, bottom, 30, 45, 35);
      CustomSound.WAR_HORN.play(online);
    });
  }

  public void sendPeaceRequest(final RaceType from, final RaceType to) {
    final Race fromRace = from.getRace();
    final Race toRace = to.getRace();
    toRace.addPeaceRequestFrom(from);
    final String msg = "§aEs gab ein Friedensangebot: §e" + fromRace.getDisplayName() + " §aan §e" + toRace.getDisplayName();
    UtilPlayer.forEachOnlineDistributed(15, (online) -> {
      UtilPlayer.playSound(online, Sound.BLOCK_NOTE_BLOCK_BELL);
      online.sendMessage(msg);
    });
  }

  public void declineRequest(final RaceType from, final RaceType to) {
    final Race fromRace = from.getRace();
    final Race toRace = to.getRace();

    fromRace.removePeaceRequest(to);

    final String msg = "§e" + fromRace.getDisplayName() + "§c hat das Friedensangebot von §e" + toRace.getDisplayName() + " §cabgelehnt.";
    for (final Player player : Bukkit.getOnlinePlayers()) {
      UtilPlayer.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 0.8F, 0.5F);
      player.sendMessage(msg);
    }
  }

  public void declarePeace(final RaceType from, final RaceType to) {
    final Race fromRace = from.getRace();
    final Race toRace = to.getRace();
    if (!fromRace.isAtWarWith(to)) {
      throw new IllegalStateException();
    }

    fromRace.removeWarEnemy(to);
    toRace.removeWarEnemy(from);
    final String top = fromRace.getIcon().getChar() + "    " + Model.PEACE_ICON.getChar() + "    " + toRace.getIcon().getChar();
    final String bottom = "Es wurde Frieden geschlossen: §e" + fromRace.getDisplayName() + " §amit§e " + toRace.getDisplayName();
    Bukkit.getOnlinePlayers().forEach(online -> {
      online.sendTitle(top, bottom, 20, 45, 30);
      CustomSound.PEACE_SOUND.play(online);
    });
  }

  @Override
  public void enable(final FeroCore plugin) {
    GsonProvider.register(Quest.class, new QuestSerializer());
    GsonProvider.register(KillQuestObjective.class, new KillQuestObjectiveSerializer());
    GsonProvider.register(GatherQuestObjective.class, new GatherQuestObjectSerializer());
    GsonProvider.register(MutableInt.class, new MutableIntSerializer());

    final FeroIO feroIO = plugin.getFeroIO();
    plugin.getPaperCommandManager()
        .getCommandCompletions()
        .registerStaticCompletion("RaceType", Arrays.stream(RaceType.values()).map(Enum::toString).collect(Collectors.toList()));

    for (final RaceType type : RaceType.values()) {
      type.setRace(feroIO.loadRace(type));
      type.getRace().init(plugin);
    }

    plugin.getPaperCommandManager().registerCommand(new RaceCommand(this));
    FeroCore.registerListener(new RaceListener(EvenDistributedPlayerTicker.start(plugin)));

    final CustomItemManager customItemManager = FeroCore.getModule(CustomItemModule.class).getCustomItemManager();
    customItemManager.registerHandle(new DwarfCanonHandle());
    customItemManager.registerHandle(new ElfOrbHandle());
    customItemManager.registerHandle(new UndeadTotemHandle());
    customItemManager.registerHandle(new CementItemHandle());
    customItemManager.registerHandle(new OrcHornItemHandle());
    customItemManager.registerHandle(new HolyBookHandle());

    final CustomRecipeManager customRecipeManager = FeroCore.getModule(CustomRecipeModule.class).getCustomRecipeManager();
    customRecipeManager.registerShapedRecipe(new DwarfCanonRecipe());
    customRecipeManager.registerShapedRecipe(new CanonBallRecipe());
    customRecipeManager.registerShapedRecipe(new ElfOrbRecipe());
    customRecipeManager.registerShapedRecipe(new CementRecipe());
    customRecipeManager.registerShapedRecipe(new UndeadTotemRecipe());
    customRecipeManager.registerShapedRecipe(new OrcHornRecipe());
    customRecipeManager.registerShapedRecipe(new HolyBookRecipe());
  }

  @Override
  public void disable(final FeroCore plugin) {
    final FeroIO feroIO = plugin.getFeroIO();
    for (final RaceType type : RaceType.values()) {
      feroIO.saveRace(type);
    }
  }
}
