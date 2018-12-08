package org.dimdev.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class ReflectionUtils {
    private static final MethodHandle addURLHandle;

    static {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            addURLHandle = MethodHandles.lookup().unreflect(method);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> T makeEnumInstance(Class<T> enumClass, Object... constructorArgs) {
        try {
            Constructor<?> constructor = enumClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);

            //noinspection unchecked
            return (T) MethodHandles.lookup().unreflectConstructor(constructor).invokeWithArguments(constructorArgs);
        } catch (Throwable t) {
            throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
        }
    }

    public static Field findField(Class<?> target, Class<?> fieldType) throws NoSuchFieldException {
    	for (Field field : target.getDeclaredFields()) {
    		if (field.getType() == fieldType) {
    			field.setAccessible(true);
    			return field;
    		}
    	}

    	throw new NoSuchFieldException("Cannot find field in " + target + '#' + fieldType);
    }

    public static Method findMethod(Class<?> target, Class<?> returnType, Class<?>... params) throws NoSuchMethodException {
    	for (Method method : target.getDeclaredMethods()) {
    		if (method.getReturnType() == returnType && Arrays.equals(method.getParameterTypes(), params)) {
    			method.setAccessible(true);
    			return method;
    		}
    	}

    	throw new NoSuchMethodException("Cannot find method in " + target + '(' + Arrays.toString(params) + ") " + returnType);
    }

    public static void addURLToClasspath(URL url) {
        try {
            addURLHandle.invoke(ClassLoader.getSystemClassLoader(), url);
        } catch (Throwable t) {
            throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
        }
    }
}
