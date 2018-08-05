package org.dimdev.utils;

import sun.reflect.ConstructorAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static <T> T makeEnumInstance(Class<T> enumClass, Object[] constructorArgs) {
        try {
            Constructor<?> constructor = enumClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);

            Field constructorAccessorField = Constructor.class.getDeclaredField("constructorAccessor");
            constructorAccessorField.setAccessible(true);
            ConstructorAccessor constructorAccessor = (ConstructorAccessor) constructorAccessorField.get(constructor);

            if (constructorAccessor == null) {
                Method acquireConstructorAccessor = Constructor.class.getDeclaredMethod("acquireConstructorAccessor");
                acquireConstructorAccessor.setAccessible(true);
                constructorAccessor = (ConstructorAccessor) acquireConstructorAccessor.invoke(constructor);
            }

            //noinspection unchecked
            return (T) constructorAccessor.newInstance(constructorArgs);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
