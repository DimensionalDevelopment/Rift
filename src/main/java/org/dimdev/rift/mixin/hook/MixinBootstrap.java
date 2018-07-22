package org.dimdev.rift.mixin.hook;

import net.minecraft.init.Bootstrap;
import org.dimdev.rift.listener.DispenserBehaviorAdder;
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
}
