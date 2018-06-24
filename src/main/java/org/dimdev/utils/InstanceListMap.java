package org.dimdev.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceListMap { // Type-safe map between classes and instances
    private Map<Class<?>, List<?>> uncheckedMap = new HashMap<>();

    public <T> void put(Class<T> key, List<T> value) {
        uncheckedMap.put(key, value);
    }

    public <T> List<T> get(Class<T> key) {
        //noinspection unchecked
        return (List<T>) uncheckedMap.get(key);
    }

    public <T> List<T> remove(Class<T> key) {
        //noinspection unchecked
        return (List<T>) uncheckedMap.remove(key);
    }

    public void clear() {
        uncheckedMap.clear();
    }

    public boolean containsKey(Class<?> key) {
        return uncheckedMap.containsKey(key);
    }

    public boolean containsValue(List<?> value) {
        return uncheckedMap.containsValue(value);
    }
}