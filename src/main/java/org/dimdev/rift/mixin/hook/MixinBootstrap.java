package org.dimdev.rift.mixin.hook;

import net.minecraft.init.Bootstrap;
import org.dimdev.rift.listener.DispenserBehaviorAdder;
import org.dimdev.rift.listener.MinecraftStartListener;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class MixinBootstrap {
    private static boolean riftAlreadyRegistered;

    @Inject(method = "registerDispenserBehaviors", at = @At("RETURN"))
    private static void onRegisterDispenserBehaviors(CallbackInfo ci) {
        for (DispenserBehaviorAdder dispenserBehaviorAdder : RiftLoader.instance.getListeners(DispenserBehaviorAdder.class)) {
            dispenserBehaviorAdder.registerDispenserBehaviors();
        }
    }

    @Inject(method = "register", at = @At("RETURN"))
    private static void onBootstrapRegister(CallbackInfo ci) {
        if (!riftAlreadyRegistered) {
            riftAlreadyRegistered = true;
            for (MinecraftStartListener listener : RiftLoader.instance.getListeners(MinecraftStartListener.class)) {
                listener.onMinecraftStart();
            }
        }
    }
}
