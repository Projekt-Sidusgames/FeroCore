package com.gestankbratwurst.ferocore.modules.racemodule.quests.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.apache.commons.lang.mutable.MutableInt;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MutableIntSerializer implements JsonDeserializer<MutableInt>, JsonSerializer<MutableInt> {

  @Override
  public MutableInt deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final JsonObject jsonObject = jsonElement.getAsJsonObject();
    return new MutableInt(jsonObject.get("value").getAsInt());
  }

  @Override
  public JsonElement serialize(final MutableInt mutableInt, final Type type, final JsonSerializationContext jsonSerializationContext) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("value", mutableInt.intValue());
    return jsonObject;
  }
}
