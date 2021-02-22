package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.gestankbratwurst.ferocore.util.common.UtilVect;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 20.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class Schematic {

  @Getter
  private String name;
  private final List<RelativeBlockData> blockDataList;

  protected static CompletableFuture<Schematic> createAsync(final Location relLoc, final BoundingBox box, final String name) {
    return CompletableFuture.supplyAsync(() -> {
      final Schematic schematic = new Schematic(name);
      final World world = relLoc.getWorld();
      final Vector relativeVec = relLoc.toVector();
      UtilVect.forEachBlock(box, world, block -> {
        final Vector pos = block.getLocation().toVector().subtract(relativeVec);
        final Material material = block.getType();
        final BlockData data = block.getBlockData();
        schematic.blockDataList.add(new RelativeBlockData(pos, material, data));
      });
      return schematic;
    });
  }

  public void fill(final Collection<RelativeBlockData> dataCollection) {
    dataCollection.addAll(this.blockDataList);
  }

  public Schematic() {
    this("-NONAME-");
  }

  public void pasteAt(final Location location, final int blocksPerTick) {
    SchematicRunnable.getInstance().addTickable(new SchematicTickable(this, location, blocksPerTick, null));
  }

  public void pasteAt(final Location location, final int blocksPerTick, final Player player) {
    SchematicRunnable.getInstance().addTickable(new SchematicTickable(this, location, blocksPerTick, player.getUniqueId()));
  }

  public Schematic(final String name) {
    this.name = name;
    this.blockDataList = new ArrayList<>();
  }

  public void importData(final JsonObject jsonObject) {
    this.name = jsonObject.get("Name").getAsString();
    for (final JsonElement element : jsonObject.get("Elements").getAsJsonArray()) {
      this.blockDataList.add(RelativeBlockData.getFromJson(element.getAsJsonObject()));
    }
  }

  public void exportData(final JsonObject jsonObject) {
    final JsonArray elements = new JsonArray();
    for (final RelativeBlockData relativeBlockData : this.blockDataList) {
      elements.add(relativeBlockData.getAsJson());
    }
    jsonObject.add("Elements", elements);
    jsonObject.addProperty("Name", this.name);
  }

  @AllArgsConstructor
  protected static class RelativeBlockData {

    public static RelativeBlockData getFromJson(final JsonObject jsonObject) {
      final Vector vector = UtilVect.vecFromString(jsonObject.get("Pos").getAsString());
      final Material material = Material.valueOf(jsonObject.get("Material").getAsString());
      final BlockData blockData = Bukkit.createBlockData(jsonObject.get("Data").getAsString());
      return new RelativeBlockData(vector, material, blockData);
    }

    private final Vector vector;
    private final Material material;
    private final BlockData blockData;

    public boolean placeRelativeTo(final Location baseLocation) {
      final Block block = baseLocation.clone().add(this.vector).getBlock();
      if (block.getType() == this.material && block.getBlockData().equals(this.blockData)) {
        return false;
      }
      block.setType(this.material);
      block.setBlockData(this.blockData);
      return true;
    }

    public JsonObject getAsJson() {
      final JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Pos", UtilVect.vecToString(this.vector));
      jsonObject.addProperty("Material", this.material.toString());
      jsonObject.addProperty("Data", this.blockData.getAsString());
      return jsonObject;
    }

  }

}
