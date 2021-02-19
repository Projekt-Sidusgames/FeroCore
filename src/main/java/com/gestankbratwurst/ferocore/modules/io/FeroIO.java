package com.gestankbratwurst.ferocore.modules.io;

import com.gestankbratwurst.ferocore.modules.playermodule.FeroPlayer;
import com.gestankbratwurst.ferocore.modules.racemodule.Race;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceType;
import com.gestankbratwurst.ferocore.util.json.GsonProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
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
public class FeroIO {

  private final File playerDataFolder;
  private final File raceFolder;
  private final FeroConfiguration feroConfiguration;

  public FeroIO(final JavaPlugin plugin) {
    final File pluginFolder = plugin.getDataFolder();
    if (!pluginFolder.exists()) {
      pluginFolder.mkdirs();
    }
    this.playerDataFolder = new File(pluginFolder, "playerdata");
    if (!this.playerDataFolder.exists()) {
      this.playerDataFolder.mkdirs();
    }
    this.raceFolder = new File(pluginFolder, "racedata");
    if (!this.raceFolder.exists()) {
      this.raceFolder.mkdirs();
    }
    final File feroConfigFile = new File(pluginFolder, "config.yml");
    if (!feroConfigFile.exists()) {
      plugin.saveResource("config.yml", false);
    }
    this.feroConfiguration = new FeroConfiguration(YamlConfiguration.loadConfiguration(feroConfigFile));
  }

  public FeroConfiguration getConfig() {
    return this.feroConfiguration;
  }

  private File getPlayerDataFile(final UUID playerID) {
    if (playerID == null) {
      throw new IllegalStateException();
    }
    return new File(this.playerDataFolder, playerID.toString() + ".json");
  }

  private File getRaceFile(final RaceType raceType) {
    return new File(this.raceFolder, raceType.toString() + ".json");
  }

  public Race loadRace(final RaceType raceType) {
    final File raceFile = this.getRaceFile(raceType);
    if (!raceFile.exists()) {
      return raceType.getRace();
    }
    try {
      final String data = Files.readString(raceFile.toPath());
      return GsonProvider.fromJson(data, raceType.getRaceClass());
    } catch (final IOException e) {
      e.printStackTrace();
      return raceType.getRace();
    }
  }

  public void saveRace(final RaceType raceType) {
    final File raceFile = this.getRaceFile(raceType);
    final String data = GsonProvider.toJson(raceType.getRace());
    try {
      Files.writeString(raceFile.toPath(), data);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void saveFeroPlayer(final FeroPlayer feroPlayer) {
    try {
      Files.writeString(this.getPlayerDataFile(feroPlayer.getPlayerID()).toPath(), GsonProvider.toJson(feroPlayer));
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public FeroPlayer loadFeroPlayer(final UUID playerID) {
    final File playerFile = this.getPlayerDataFile(playerID);
    if (!playerFile.exists()) {
      return new FeroPlayer(playerID);
    }
    try {
      return GsonProvider.fromJson(Files.readString(playerFile.toPath()), FeroPlayer.class);
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public Collection<FeroPlayer> loadAllFeroPlayers() {
    final List<FeroPlayer> playerList = new ArrayList<>();
    final File[] files = this.playerDataFolder.listFiles();
    if (files == null) {
      return playerList;
    }
    for (final File playerFile : files) {
      final UUID uuid = UUID.fromString(playerFile.getName().split("\\.")[0]);
      playerList.add(this.loadFeroPlayer(uuid));
    }
    return playerList;
  }

  public void saveAllFeroPlayer(final Collection<FeroPlayer> players) {
    players.forEach(this::saveFeroPlayer);
  }

}
