package org.dimdev.rift.mixin.hook;

import net.minecraft.particles.ParticleType;
import org.dimdev.rift.listener.ParticleTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleType.class)
public abstract class MixinParticleType {
    @Inject(method = "registerAll", at = @At("RETURN"))
    private static void onRegisterParticleTypes(CallbackInfo ci) {
        for (ParticleTypeAdder particleTypeAdder : RiftLoader.instance.getListeners(ParticleTypeAdder.class)) {
            particleTypeAdder.registerParticles();
        }
    }
}
