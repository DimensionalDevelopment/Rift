package org.dimdev.rift.listener;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;

public interface FluidAdder {
    interface FluidRegistrationReceiver {
        void registerFluid(ResourceLocation location, Fluid block);
    }

    void registerFluids(FluidRegistrationReceiver receiver);
}
