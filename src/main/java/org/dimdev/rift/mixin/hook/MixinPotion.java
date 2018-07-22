package org.dimdev.rift.mixin.hook;

import net.minecraft.potion.Potion;
import org.dimdev.rift.listener.MobEffectAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Potion.class)
public class MixinPotion {
    @Inject(method = "registerPotions", at = @At(value = "RETURN"))
    private static void onRegisterPotions(CallbackInfo ci) {
        for (MobEffectAdder mobEffectAdder : RiftLoader.instance.getListeners(MobEffectAdder.class)) {
            mobEffectAdder.registerMobEffects();
        }
    }
}
