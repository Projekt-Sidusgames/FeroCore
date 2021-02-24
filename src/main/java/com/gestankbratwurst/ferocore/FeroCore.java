package com.gestankbratwurst.ferocore;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.custommob.CustomMobModule;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeModule;
import com.gestankbratwurst.ferocore.modules.customtiles.CustomTileModule;
import com.gestankbratwurst.ferocore.modules.io.FeroIO;
import com.gestankbratwurst.ferocore.modules.playermodule.PlayerModule;
import com.gestankbratwurst.ferocore.modules.protectionmodule.ProtectionModule;
import com.gestankbratwurst.ferocore.modules.racemodule.RaceModule;
import com.gestankbratwurst.ferocore.modules.rolemodule.RoleModule;
import com.gestankbratwurst.ferocore.modules.skillmodule.SkillModule;
import com.gestankbratwurst.ferocore.resourcepack.ResourcepackModule;
import com.gestankbratwurst.ferocore.util.UtilModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class FeroCore extends JavaPlugin {

  private static FeroCore instance;
  private final Map<Class<? extends BaseModule>, BaseModule> modules = new LinkedHashMap<>();
  @Getter
  private FeroIO feroIO;
  @Getter
  private PaperCommandManager paperCommandManager;
  @Getter
  private ProtocolManager protocolManager;

  public static <T extends BaseModule> T getModule(final Class<T> clazz) {
    return (T) instance.modules.get(clazz);
  }

  @Override
  public void onEnable() {
    instance = this;
    this.protocolManager = ProtocolLibrary.getProtocolManager();
    this.paperCommandManager = new PaperCommandManager(this);
    this.feroIO = new FeroIO(this);
    this.modules.put(UtilModule.class, new UtilModule());
    this.modules.put(ResourcepackModule.class, new ResourcepackModule());
    this.modules.put(ProtectionModule.class, new ProtectionModule());
    this.modules.put(SkillModule.class, new SkillModule());
    this.modules.put(CustomTileModule.class, new CustomTileModule());
    this.modules.put(CustomItemModule.class, new CustomItemModule());
    this.modules.put(CustomRecipeModule.class, new CustomRecipeModule());
    this.modules.put(CustomMobModule.class, new CustomMobModule());
    this.modules.put(RaceModule.class, new RaceModule());
    this.modules.put(RoleModule.class, new RoleModule());
    this.modules.put(PlayerModule.class, new PlayerModule());
    this.enableModules();
  }

  @Override
  public void onDisable() {
    this.disableModules();
  }


  private void enableModules() {
    this.getLogger().info("Enabling modules...");
    for (final BaseModule module : this.modules.values()) {
      this.getLogger().info("Starting " + module.getClass().getSimpleName().split("\\.")[0]);
      module.enable(this);
    }
  }

  private void disableModules() {
    final List<BaseModule> values = new ArrayList<>(this.modules.values());
    Collections.reverse(values);
    for (final BaseModule module : values) {
      module.disable(this);
    }
  }

  public static void registerListener(final Listener listener) {
    Bukkit.getPluginManager().registerEvents(listener, instance);
  }

}
