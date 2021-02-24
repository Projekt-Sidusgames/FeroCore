package com.gestankbratwurst.ferocore.modules.racemodule.quests.json;

import com.gestankbratwurst.ferocore.modules.racemodule.quests.GatherQuest;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.KillQuest;
import com.gestankbratwurst.ferocore.modules.racemodule.quests.Quest;
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
public class QuestSerializer implements JsonSerializer<Quest>, JsonDeserializer<Quest> {

  @Override
  public Quest deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final JsonObject jsonObject = jsonElement.getAsJsonObject();
    final String questType = jsonObject.get("Type").getAsString();

    if (questType.equals("Kill")) {
      return jsonDeserializationContext.deserialize(jsonObject.get("QuestData").getAsJsonObject(), KillQuest.class);
    } else if (questType.equals("Gather")) {
      return jsonDeserializationContext.deserialize(jsonObject.get("QuestData").getAsJsonObject(), GatherQuest.class);
    }

    return null;
  }

  @Override
  public JsonElement serialize(final Quest quest, final Type type, final JsonSerializationContext jsonSerializationContext) {
    final JsonObject jsonObject = new JsonObject();
    if (quest instanceof KillQuest) {
      jsonObject.addProperty("Type", "Kill");
      jsonObject.add("QuestData", jsonSerializationContext.serialize(quest));
    } else if (quest instanceof GatherQuest) {
      jsonObject.addProperty("Type", "Gather");
      jsonObject.add("QuestData", jsonSerializationContext.serialize(quest));
    }

    return jsonObject;
  }
}
