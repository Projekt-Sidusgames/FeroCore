package com.gestankbratwurst.ferocore.resourcepack;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.resourcepack.distribution.ResourcepackListener;
import com.gestankbratwurst.ferocore.resourcepack.distribution.ResourcepackManager;
import com.gestankbratwurst.ferocore.resourcepack.packing.AssetLibrary;
import com.gestankbratwurst.ferocore.resourcepack.packing.ResourcepackAssembler;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;

public class ResourcepackModule implements BaseModule {

  private ResourcepackManager resourcepackManager;
  private AssetLibrary assetLibrary;

  @Override
  public void enable(final FeroCore plugin) {
    this.assetLibrary = new AssetLibrary(plugin);
    CompletableFuture.runAsync(() -> {
      try {
        new ResourcepackAssembler(plugin, this.assetLibrary).zipResourcepack();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }).thenRun(() -> {
      try {
        this.resourcepackManager = new ResourcepackManager();
      } catch (final Exception exception) {
        exception.printStackTrace();
        Bukkit.shutdown();
      }
      FeroCore.registerListener(new ResourcepackListener(plugin, this.resourcepackManager));
    });
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
