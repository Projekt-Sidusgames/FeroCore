package com.gestankbratwurst.ferocore.modules.protectionmodule;

import com.gestankbratwurst.ferocore.util.json.GsonProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 20.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SchematicManager {

  private final Map<String, Schematic> schematicMap;

  public SchematicManager() {
    this.schematicMap = new ConcurrentHashMap<>();
  }

  public List<String> getSchematicNames() {
    return new ArrayList<>(this.schematicMap.keySet());
  }

  public CompletableFuture<Schematic> createSchematicAsync(final Location baseLoc, final BoundingBox box, final String name) {
    return Schematic.createAsync(baseLoc, box, name).thenApply(schematic -> {
      this.schematicMap.put(schematic.getName(), schematic);
      return schematic;
    });
  }

  public Schematic getSchematic(final String name) {
    return this.schematicMap.get(name);
  }

  public void load(final String data) {
    if (data == null) {
      return;
    }
    final JsonObject jsonObject = GsonProvider.fromJson(data, JsonObject.class);
    final JsonArray schematicArray = jsonObject.get("Schematics").getAsJsonArray();
    for (final JsonElement element : schematicArray) {
      final Schematic schematic = new Schematic();
      schematic.importData(element.getAsJsonObject());
      this.schematicMap.put(schematic.getName(), schematic);
    }
  }

  public String save() {
    final JsonObject jsonObject = new JsonObject();
    final JsonArray schematicArray = new JsonArray();
    for (final Schematic schematic : this.schematicMap.values()) {
      final JsonObject schematicJson = new JsonObject();
      schematic.exportData(schematicJson);
      schematicArray.add(schematicJson);
    }
    jsonObject.add("Schematics", schematicArray);
    return GsonProvider.toJson(jsonObject);
  }

}