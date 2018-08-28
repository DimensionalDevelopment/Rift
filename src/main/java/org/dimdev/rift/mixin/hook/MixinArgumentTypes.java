package org.dimdev.rift.mixin.hook;

import net.minecraft.command.arguments.ArgumentTypes;
import org.dimdev.rift.listener.ArgumentTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArgumentTypes.class)
public class MixinArgumentTypes {
    @Inject(method = "registerArgumentTypes", at = @At("RETURN"))
    private static void onRegisterArgumentTypes(CallbackInfo ci) {
        for (ArgumentTypeAdder argumentTypeAdder : RiftLoader.instance.getListeners(ArgumentTypeAdder.class)) {
            argumentTypeAdder.addArgumentTypes();
        }
    }
}
