package org.dimdev.rift.listener.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvent;
import org.dimdev.rift.listener.ListenerInterface;
import org.dimdev.utils.ReflectionUtils;

import static net.minecraft.client.audio.MusicTicker.MusicType;

@ListenerInterface
public interface AmbientMusicTypeProvider {
    static MusicType newMusicType(String name, SoundEvent sound, int minDelay, int maxDelay) {
        return ReflectionUtils.makeEnumInstance(MusicType.class, name, -1, sound, minDelay, maxDelay);
    }

    MusicType getAmbientMusicType(Minecraft client);
}
