package org.dimdev.rift.mixin.hook;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.dimdev.rift.listener.MobEffectAdder;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Potion.class)
public class MixinPotion {
    @Shadow @Final public static RegistryNamespaced<ResourceLocation, Potion> REGISTRY;

    @Inject(method = "registerPotions", at = @At(value = "RETURN"))
    private static void onRegisterPotions(CallbackInfo ci) {
        for (MobEffectAdder mobEffectAdder : SimpleLoader.instance.getListeners(MobEffectAdder.class)) {
            mobEffectAdder.registerMobEffects(REGISTRY::putObject);
        }
    }
}
