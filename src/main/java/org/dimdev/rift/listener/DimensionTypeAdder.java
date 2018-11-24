package org.dimdev.rift.listener;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.Supplier;

public interface DimensionTypeAdder {
    static DimensionType addDimensionType(int id, ResourceLocation name, String suffix, Supplier<? extends Dimension> dimensionSupplier) {
    	return DimensionType.register(name.toString(), new DimensionType(id, suffix, name.getPath(), dimensionSupplier));
    }

    void registerDimensionTypes();
}
