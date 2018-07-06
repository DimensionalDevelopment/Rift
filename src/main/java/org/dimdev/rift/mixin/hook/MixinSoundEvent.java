package org.dimdev.rift.mixin.hook;

import net.minecraft.util.SoundEvent;
import org.dimdev.rift.listener.SoundAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEvent.class)
public abstract class MixinSoundEvent {
    @Shadow private static void registerSound(String soundNameIn) {}

    @Inject(method = "registerSounds", at = @At("RETURN"))
    private static void onRegisterSounds(CallbackInfo ci) {
        for (SoundAdder soundAdder : RiftLoader.instance.getListeners(SoundAdder.class)) {
            for (String name : soundAdder.getSoundEventNames()) {
                registerSound(name);
            }
        }
    }
}
