package org.dimdev.utils;

import java.util.HashMap;
import java.util.Map;

public class InstanceMap { // Type-safe map between classes and instances
    private Map<Class<?>, Object> uncheckedMap = new HashMap<>();

    public <T> void put(Class<T> key, T value) {
        uncheckedMap.put(key, value);
    }

    public void castAndPut(Class<?> key, Object value) {
        uncheckedMap.put(key, key.cast(value));
    }

    public <T> T get(Class<T> key) {
        //noinspection unchecked
        return (T) uncheckedMap.get(key);
    }

    public <T> T remove(Class<T> key) {
        //noinspection unchecked
        return (T) uncheckedMap.remove(key);
    }

    public void clear() {
        uncheckedMap.clear();
    }

    public boolean containsKey(Class<?> key) {
        return uncheckedMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return uncheckedMap.containsValue(value);
    }
}