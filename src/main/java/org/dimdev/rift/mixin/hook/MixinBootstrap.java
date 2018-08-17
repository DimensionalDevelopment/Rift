package org.dimdev.rift.mixin.hook;

import net.minecraft.init.Bootstrap;
import org.dimdev.rift.listener.BootstrapListener;
import org.dimdev.rift.listener.DispenserBehaviorAdder;
import org.dimdev.rift.listener.MinecraftStartListener;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class MixinBootstrap {
    @Inject(method = "registerDispenserBehaviors", at = @At("RETURN"))
    private static void onRegisterDispenserBehaviors(CallbackInfo ci) {
        for (DispenserBehaviorAdder dispenserBehaviorAdder : RiftLoader.instance.getListeners(DispenserBehaviorAdder.class)) {
            dispenserBehaviorAdder.registerDispenserBehaviors();
        }
    }

    @Inject(method = "register", at = @At("HEAD"))
    private static void beforeBootstrapRegister(CallbackInfo ci) {
        for (MinecraftStartListener listener : RiftLoader.instance.getListeners(MinecraftStartListener.class)) {
            listener.onMinecraftStart();
        }
    }

    @Inject(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/init/Bootstrap;redirectOutputToLog()V"))
    private static void afterBootstrapRegister(CallbackInfo ci) {
        for (BootstrapListener listener : RiftLoader.instance.getListeners(BootstrapListener.class)) {
            listener.afterVanillaBootstrap();
        }
    }
}
