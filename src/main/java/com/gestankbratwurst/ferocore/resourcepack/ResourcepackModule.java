package com.gestankbratwurst.ferocore.resourcepack;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.resourcepack.distribution.ResourcepackListener;
import com.gestankbratwurst.ferocore.resourcepack.distribution.ResourcepackManager;
import com.gestankbratwurst.ferocore.resourcepack.packing.AssetLibrary;
import com.gestankbratwurst.ferocore.resourcepack.packing.ResourcepackAssembler;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import org.bukkit.Bukkit;

public class ResourcepackModule implements BaseModule {

  @Getter
  private static boolean serverRunning = false;

  private ResourcepackManager resourcepackManager;
  private AssetLibrary assetLibrary;

  @Override
  public void enable(final FeroCore plugin) {
    this.assetLibrary = new AssetLibrary(plugin);
    plugin.getLogger().info("Blocking main thread. Waiting for resourcepack server to start.");
    CompletableFuture.runAsync(() -> {
      try {
        new ResourcepackAssembler(plugin, this.assetLibrary).zipResourcepack();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }).thenRun(() -> {
      try {
        this.resourcepackManager = new ResourcepackManager((v) -> serverRunning = true);
      } catch (final Exception exception) {
        exception.printStackTrace();
        Bukkit.shutdown();
      }
      FeroCore.registerListener(new ResourcepackListener(plugin, this.resourcepackManager));
    }).join();
  }

  @Override
  public void disable(final FeroCore plugin) {
    if (this.resourcepackManager == null) {
      System.out.println("§c ResourcepackManager is null.");
    } else {
      this.resourcepackManager.shutdown();
    }
    this.assetLibrary.saveCache();
  }
}
