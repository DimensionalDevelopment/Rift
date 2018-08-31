package org.dimdev.utils;

import org.dimdev.riftloader.RiftLoader;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ReflectionUtils {
    private static final MethodHandle addURLHandle;

    static {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            addURLHandle = MethodHandles.lookup().unreflect(method);
        } catch(ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> T makeEnumInstance(Class<T> enumClass, Object... constructorArgs) {
        try {
            Constructor<?> constructor = enumClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);

            //noinspection unchecked
            return (T) MethodHandles.lookup().unreflectConstructor(constructor).invokeWithArguments(constructorArgs);
        } catch(Throwable t) {
            throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
        }
    }

    public static void addURLToClasspath(URL url) {
        try {
            addURLHandle.invoke(ClassLoader.getSystemClassLoader(), url);
        } catch(Throwable t) {
            throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class<? super E> type, E instance, String... fieldNames) {
        try {
            return (T) findField(type, fieldNames).get(instance);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field findField(Class clazz, String... fieldNames) {
        for(String name : fieldNames) {
            try {
                Field f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch(NoSuchFieldException e) {
                //ignore
            }
        }
        RiftLoader.getLogger().error("Reflection error in class {}, cannot find any of those fields: {}", clazz.getCanonicalName(), String.join(", ", fieldNames));
        throw new RuntimeException();
    }

    public static <T, E> void setPrivateValue(Class<? super T> type, T instance, E value, String... fieldNames) {
        try {
            findField(type, fieldNames).set(instance, value);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
