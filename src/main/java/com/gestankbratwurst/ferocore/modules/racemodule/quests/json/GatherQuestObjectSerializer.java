package com.gestankbratwurst.ferocore.modules.racemodule.quests.json;

import com.gestankbratwurst.ferocore.modules.racemodule.quests.GatherQuestObjective;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.SimpleGatherQuestObjective;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 22.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class GatherQuestObjectSerializer implements JsonSerializer<GatherQuestObjective>, JsonDeserializer<GatherQuestObjective> {

  @Override
  public GatherQuestObjective deserialize(final JsonElement jsonElement, final Type type,
      final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final JsonObject jsonObject = jsonElement.getAsJsonObject();
    final String objectiveType = jsonObject.get("Type").getAsString();
    if (objectiveType.equals("Simple")) {
      return this.deserializeSimple(jsonObject);
    }
    return null;
  }

  @Override
  public JsonElement serialize(final GatherQuestObjective gatherQuestObjective, final Type type,
      final JsonSerializationContext jsonSerializationContext) {
    if (gatherQuestObjective instanceof SimpleGatherQuestObjective) {
      return this.serializeSimple((SimpleGatherQuestObjective) gatherQuestObjective);
    }
    return null;
  }

  private JsonElement serializeSimple(final SimpleGatherQuestObjective objective) {
    final JsonObject jsonObject = new JsonObject();

    jsonObject.addProperty("Type", "Simple");
    jsonObject.addProperty("Value", objective.toString());

    return jsonObject;
  }

  private SimpleGatherQuestObjective deserializeSimple(final JsonObject jsonObject) {
    return SimpleGatherQuestObjective.of(jsonObject.get("Value").getAsString());
  }

}
