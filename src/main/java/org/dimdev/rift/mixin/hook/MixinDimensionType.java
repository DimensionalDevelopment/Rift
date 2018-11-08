package org.dimdev.rift.mixin.hook;

import net.minecraft.world.dimension.DimensionType;

import org.dimdev.rift.listener.DimensionTypeAdder;
import org.dimdev.riftloader.RiftLoader;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(DimensionType.class)
public class MixinDimensionType {
    static {
        for (DimensionTypeAdder dimensionTypeAdder : RiftLoader.instance.getListeners(DimensionTypeAdder.class)) {
        	dimensionTypeAdder.registerDimensionTypes();
        }
    }
}
