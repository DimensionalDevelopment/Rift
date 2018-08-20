package org.dimdev.rift.listener;

import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import org.dimdev.utils.ReflectionUtils;

import java.util.Set;
import java.util.function.Supplier;

public interface DimensionTypeAdder {
    static DimensionType newDimensionType(int id, String name, String suffix, Supplier<? extends Dimension> dimensionSupplier) {
        return ReflectionUtils.makeEnumInstance(DimensionType.class, name.toUpperCase(), -1, id, name, suffix, dimensionSupplier);
    }

    Set<? extends DimensionType> getDimensionTypes();
}
