package org.dimdev.riftloader;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModInfo {
    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Listener.class, (JsonSerializer<Listener>) (listener, type, context) -> {
                if (listener.priority == 0 && listener.sides == Sides.BOTH) {
                    return new JsonPrimitive(listener.className);
                }

                return new Gson().toJsonTree(listener);
            })
            .registerTypeAdapter(Listener.class, (JsonDeserializer<Listener>) (json, type, context) -> {
                if (json.isJsonPrimitive() && json.isJsonPrimitive() && ((JsonPrimitive) json).isString()) {
                    return new Listener(json.getAsString());
                }

                return new Gson().fromJson(json, type);
            })
            .create();

    public enum Sides {
        @SerializedName("client") CLIENT,
        @SerializedName("server") SERVER,
        @SerializedName("both") BOTH;

        public boolean includes(Sides sides) {
            return this == BOTH || this == sides;
        }
    }

    public static class Listener {
        @SerializedName("class") public String className;
        public int priority = 0;
        @SerializedName("side") public Sides sides = Sides.BOTH;

        public Listener(String className) {
            this.className = className;
        }
    }

    public File source;

    public String id;
    public String name;
    public List<String> authors = new ArrayList<>();
    public List<Listener> listeners = new ArrayList<>();
}
