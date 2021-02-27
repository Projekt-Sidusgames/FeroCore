package com.gestankbratwurst.ferocore.util;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.resourcepack.skins.Model;
import com.gestankbratwurst.ferocore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.ferocore.util.actionbar.ActionBarManager;
import com.gestankbratwurst.ferocore.util.common.BukkitTime;
import com.gestankbratwurst.ferocore.util.common.ChatInput;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import com.gestankbratwurst.ferocore.util.common.UtilBlock;
import com.gestankbratwurst.ferocore.util.common.UtilItem;
import com.gestankbratwurst.ferocore.util.common.UtilMobs;
import com.gestankbratwurst.ferocore.util.common.UtilPlayer;
import com.gestankbratwurst.ferocore.util.items.display.ItemDisplayCompiler;
import com.gestankbratwurst.ferocore.util.json.GsonProvider;
import com.gestankbratwurst.ferocore.util.json.ItemStackArraySerializer;
import com.gestankbratwurst.ferocore.util.json.ItemStackSerializer;
import com.gestankbratwurst.ferocore.util.json.LocationSerializer;
import com.gestankbratwurst.ferocore.util.json.MultimapSerializer;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import net.crytec.inventoryapi.InventoryAPI;
import net.crytec.libs.protocol.ProtocolAPI;
import net.crytec.libs.protocol.holograms.impl.HologramManager;
import net.crytec.libs.protocol.holograms.impl.infobar.InfoBar;
import net.crytec.libs.protocol.holograms.infobars.InfoBarManager;
import net.crytec.libs.protocol.npc.NpcAPI;
import net.crytec.libs.protocol.skinclient.PlayerSkinManager;
import net.crytec.libs.protocol.tablist.TabListManager;
import net.crytec.libs.protocol.tablist.implementation.EmptyTablist;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class UtilModule implements BaseModule {

  @Getter
  private HologramManager hologramManager;
  @Getter
  private ActionBarManager actionBarManager;
  @Getter
  private InfoBarManager infoBarManager;
  @Getter
  private ProtocolAPI protocolAPI;
  @Getter
  private NpcAPI npcAPI;
  @Getter
  private TabListManager tabListManager;
  @Getter
  private PlayerSkinManager playerSkinManager;
  @Getter
  private ItemDisplayCompiler displayCompiler;

  @Override
  public void enable(final FeroCore plugin) {
    plugin.getPaperCommandManager().registerCommand(new DebugCommand());
    plugin.getPaperCommandManager().getCommandCompletions()
        .registerStaticCompletion("Models", Arrays.stream(Model.values()).map(Enum::toString).collect(Collectors.toList()));
    plugin.getPaperCommandManager().getCommandCompletions()
        .registerStaticCompletion("Skins",
            Arrays.stream(Model.values()).filter(Model::isHeadSkinEnabled).map(Enum::toString).collect(Collectors.toList()));
    plugin.getPaperCommandManager().getCommandCompletions()
        .registerStaticCompletion("CustomSounds", Arrays.stream(CustomSound.values()).map(Enum::toString).collect(Collectors.toList()));

    GsonProvider.register(ItemStack.class, new ItemStackSerializer());
    GsonProvider.register(CraftItemStack.class, new ItemStackSerializer());
    GsonProvider.register(ItemStack[].class, new ItemStackArraySerializer());
    GsonProvider.register(CraftItemStack[].class, new ItemStackArraySerializer());
    GsonProvider.register(Location.class, new LocationSerializer());
    GsonProvider.register(Multimap.class, new MultimapSerializer());

    BukkitTime.start(plugin);
    ChatInput.init(plugin);
    NameSpaceFactory.init(plugin);
    UtilPlayer.init(plugin);
    UtilBlock.init(plugin);
    UtilMobs.init(plugin);
    UtilItem.init(plugin);

    InventoryAPI.init(plugin);

    this.displayCompiler = new ItemDisplayCompiler(plugin);
    plugin.getProtocolManager().addPacketListener(this.displayCompiler);
    this.hologramManager = new HologramManager(plugin);
    this.playerSkinManager = new PlayerSkinManager();
    this.actionBarManager = new ActionBarManager(plugin);
    this.infoBarManager = new InfoBarManager(plugin, (entity) -> new InfoBar(entity, this.infoBarManager));
    this.protocolAPI = new ProtocolAPI(plugin);
    this.npcAPI = new NpcAPI(plugin);
    final EmptyTablist et = new EmptyTablist(this.tabListManager);
    et.setFooter("Line 1\nLine 2");
    this.tabListManager = new TabListManager(plugin, (p) -> et);
  }

  @Override
  public void disable(final FeroCore plugin) {
    UtilBlock.terminate(plugin);
  }

}
