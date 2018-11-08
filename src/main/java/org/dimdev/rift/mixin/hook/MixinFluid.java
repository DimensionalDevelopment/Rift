package org.dimdev.rift.mixin.hook;

import net.minecraft.fluid.Fluid;
import org.dimdev.rift.listener.FluidAdder;
import org.dimdev.riftloader.RiftLoader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fluid.class)
public abstract class MixinFluid {
	@Inject(method = "registerAll", at = @At(value = "FIELD", target = "Lnet/minecraft/util/registry/IRegistry;FLUID:Lnet/minecraft/util/registry/IRegistry;", opcode = Opcodes.GETSTATIC, ordinal = 1))
    private static void onRegisterFluids(CallbackInfo ci) {
        for (FluidAdder fluidAdder : RiftLoader.instance.getListeners(FluidAdder.class)) {
            fluidAdder.registerFluids();
        }
    }
}
