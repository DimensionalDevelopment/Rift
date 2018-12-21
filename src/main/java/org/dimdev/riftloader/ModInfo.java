package org.dimdev.riftloader;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModInfo {
    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Listener.class, (JsonSerializer<Listener>) (listener, type, context) -> {
                if (listener.priority == 0 && listener.side == Side.BOTH) {
                    return new JsonPrimitive(listener.className);
                }

                return new Gson().toJsonTree(listener);
            })
            .registerTypeAdapter(Listener.class, (JsonDeserializer<Listener>) (json, type, context) -> {
                if (json.isJsonPrimitive() && ((JsonPrimitive) json).isString()) {
                    return new Listener(json.getAsString());
                }

                Listener listener = new Gson().fromJson(json, Listener.class);
                if (listener.className == null) throw new JsonSyntaxException("Listener with no class!");
                //If the side isn't provided Gson helpfully returns null
                if (listener.side == null) listener.side = Side.BOTH;
                return listener;
            })
            .create();

    public static class Listener {
        @SerializedName("class") public String className;
        public int priority = 0;
        public Side side = Side.BOTH;

        public Listener(String className) {
            this.className = className;
        }
    }

    public File source;

    public String id;
    public String name;
    public String description;
    public String version;
    public String url;
    public List<String> authors = new ArrayList<>();
    public List<Listener> listeners = new ArrayList<>();
}
