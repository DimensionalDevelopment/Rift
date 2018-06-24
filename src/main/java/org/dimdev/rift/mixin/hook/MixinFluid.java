package org.dimdev.rift.mixin.hook;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.FluidAdder;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fluid.class)
public abstract class MixinFluid {
    @Shadow private static void registerFluid(ResourceLocation p_207194_0_, Fluid p_207194_1_) {}
    private static final FluidAdder.FluidRegistrationReceiver FLUID_REGISTRATION_RECEIVER = MixinFluid::registerFluid;

    @Inject(method = "registerFluids", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespacedDefaultedByKey;validateKey()V"))
    private static void onRegisterBlocks(CallbackInfo ci) {
        for (FluidAdder fluidAdder : SimpleLoader.instance.getListeners(FluidAdder.class)) {
            fluidAdder.registerFluids(FLUID_REGISTRATION_RECEIVER);
        }
    }
}
