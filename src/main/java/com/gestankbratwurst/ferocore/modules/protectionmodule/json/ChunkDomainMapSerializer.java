package com.gestankbratwurst.ferocore.modules.protectionmodule.json;

import com.gestankbratwurst.ferocore.modules.protectionmodule.ChunkDomain;
import com.gestankbratwurst.ferocore.modules.protectionmodule.ChunkDomainMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.lang.reflect.Type;
import java.util.Map.Entry;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of PSSCore and was created at the 06.11.2020
 *
 * PSSCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ChunkDomainMapSerializer implements JsonSerializer<ChunkDomainMap>, JsonDeserializer<ChunkDomainMap> {

  @Override
  public ChunkDomainMap deserialize(final JsonElement jsonElement, final Type type,
      final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final JsonObject jsonObject = jsonElement.getAsJsonObject();

    final ChunkDomainMap map = new ChunkDomainMap();

    for (final Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      map.put(Long.valueOf(entry.getKey()).longValue(), jsonDeserializationContext.deserialize(entry.getValue(), ChunkDomain.class));
    }

    return map;
  }

  @Override
  public JsonElement serialize(final ChunkDomainMap chunkDomainMap, final Type type,
      final JsonSerializationContext jsonSerializationContext) {
    final JsonObject jsonObject = new JsonObject();
    for (final Long2ObjectMap.Entry<ChunkDomain> entry : chunkDomainMap.long2ObjectEntrySet()) {
      jsonObject.add("" + entry.getLongKey(), jsonSerializationContext.serialize(entry.getValue()));
    }
    return jsonObject;
  }
}
