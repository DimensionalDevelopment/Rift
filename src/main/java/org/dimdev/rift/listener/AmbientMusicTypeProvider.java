package org.dimdev.rift.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.SoundEvent;
import sun.reflect.ConstructorAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static net.minecraft.client.audio.MusicTicker.*;

public interface AmbientMusicTypeProvider {
    public static MusicType newMusicType(String name, SoundEvent sound, int minDelay, int maxDelay) {
        try {
            Constructor<MusicType> constructor = MusicType.class.getDeclaredConstructor(String.class, int.class, SoundEvent.class, int.class, int.class);
            constructor.setAccessible(true);

            Field constructorAccessorField = Constructor.class.getDeclaredField("constructorAccessor");
            constructorAccessorField.setAccessible(true);
            ConstructorAccessor constructorAccessor = (ConstructorAccessor) constructorAccessorField.get(constructor);

            if (constructorAccessor == null) {
                Method acquireConstructorAccessor = Constructor.class.getDeclaredMethod("acquireConstructorAccessor");
                acquireConstructorAccessor.setAccessible(true);
                constructorAccessor = (ConstructorAccessor) acquireConstructorAccessor.invoke(constructor);
            }

            return (MusicType) constructorAccessor.newInstance(new Object[] { name, -1, sound, minDelay, maxDelay });
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public MusicType getAmbientMusicType(Minecraft client);
}
