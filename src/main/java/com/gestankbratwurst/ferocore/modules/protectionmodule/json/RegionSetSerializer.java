package com.gestankbratwurst.ferocore.modules.protectionmodule.json;

import com.gestankbratwurst.ferocore.modules.protectionmodule.ProtectedRegion;
import com.gestankbratwurst.ferocore.modules.protectionmodule.RegionSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of PSSCore and was created at the 06.11.2020
 *
 * PSSCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RegionSetSerializer implements JsonDeserializer<RegionSet>, JsonSerializer<RegionSet> {

  @Override
  public RegionSet deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final JsonArray jsonArray = jsonElement.getAsJsonArray();
    final RegionSet regionSet = new RegionSet();
    for (final JsonElement element : jsonArray) {
      regionSet.add(jsonDeserializationContext.deserialize(element, type));
    }
    return regionSet;
  }

  @Override
  public JsonElement serialize(final RegionSet regionSet, final Type type, final JsonSerializationContext jsonSerializationContext) {
    final JsonArray jsonArray = new JsonArray();

    for (final ProtectedRegion element : regionSet) {
      jsonArray.add(jsonSerializationContext.serialize(element));
    }

    return jsonArray;
  }
}
