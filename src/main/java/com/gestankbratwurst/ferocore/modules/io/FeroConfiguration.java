package com.gestankbratwurst.ferocore.modules.io;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class FeroConfiguration {

  public FeroConfiguration(final FileConfiguration fileConfiguration) {
    this.resourcePackServerPort = fileConfiguration.getInt("ResourcePack.ServerPort", 9988);
    this.resourcePackServerIP = fileConfiguration.getString("ResourcePack.ServerIP", "127.0.0.1");
    this.schedulerThreadPoolSize = fileConfiguration.getInt("SchedulerThreadPoolSize", 2);
  }

  @Getter
  private final String resourcePackServerIP;
  @Getter
  private final int resourcePackServerPort;
  @Getter
  private final int schedulerThreadPoolSize;

}
