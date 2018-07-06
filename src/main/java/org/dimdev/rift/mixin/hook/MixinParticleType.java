package org.dimdev.rift.mixin.hook;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.dimdev.rift.listener.ParticleTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;

@Mixin(ParticleType.class)
public abstract class MixinParticleType {
    @Shadow @Final public static RegistryNamespaced<ResourceLocation, ParticleType<? extends IParticleData>> REGISTRY;

    private static BasicParticleType newBasicParticleType(ResourceLocation resourceLocation, boolean alwaysRender) {
        try {
            Constructor<BasicParticleType> constructor = BasicParticleType.class.getDeclaredConstructor(ResourceLocation.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(resourceLocation, alwaysRender);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends IParticleData> ParticleType<T> newParticleType(ResourceLocation resourceLocation, boolean alwaysRender, IParticleData.IDeserializer<T> deserializer) {
        try {
            Constructor<ParticleType> constructor = ParticleType.class.getDeclaredConstructor(ResourceLocation.class, boolean.class, IParticleData.IDeserializer.class);
            constructor.setAccessible(true);
            return (ParticleType<T>) constructor.newInstance(resourceLocation, alwaysRender, deserializer);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ParticleTypeAdder.ParticleRegistrationReceiver PARTICLE_REGISTRATION_RECEIVER = new ParticleTypeAdder.ParticleRegistrationReceiver() {
        @Override
        public void registerParticleType(ResourceLocation resourceLocation, boolean alwaysRender) {
            REGISTRY.putObject(resourceLocation, newBasicParticleType(resourceLocation, alwaysRender));
        }

        @Override
        public <T extends IParticleData> void registerParticleType(ResourceLocation resourceLocation, boolean alwaysRender, IParticleData.IDeserializer<T> deserializer) {
            REGISTRY.putObject(resourceLocation, newParticleType(resourceLocation, alwaysRender, deserializer));
        }
    };

    @Inject(method = "registerParticleTypes", at = @At("RETURN"))
    private static void onRegisterParticleTypes(CallbackInfo ci) {
        for (ParticleTypeAdder particleTypeAdder : RiftLoader.instance.getListeners(ParticleTypeAdder.class)) {
            particleTypeAdder.registerParticles(PARTICLE_REGISTRATION_RECEIVER);
        }
    }
}
