package org.dimdev.rift.listener;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;

public interface ParticleTypeAdder {
    interface ParticleRegistrationReceiver {
        void registerParticleType(ResourceLocation resourceLocation, boolean alwaysRender);
        <T extends IParticleData> void registerParticleType(ResourceLocation resourceLocation, boolean alwaysRender, IParticleData.IDeserializer<T> deserializer);
    }

    void registerParticles(ParticleRegistrationReceiver receiver);
}
