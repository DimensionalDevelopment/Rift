package org.dimdev.rift.mixin.hook;

import net.minecraft.world.dimension.DimensionType;
import org.dimdev.rift.listener.DimensionTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashMap;

@Mixin(DimensionType.class)
public class MixinDimensionType {
    @SuppressWarnings("PublicStaticMixinMember") // accessed using reflection
    private static HashMap<Integer, DimensionType> dimensionTypes = new HashMap<>();

    static {
        for (DimensionType dimensionType : DimensionType.values()) {
            dimensionTypes.put(dimensionType.getId(), dimensionType);
        }

        for (DimensionTypeAdder dimensionTypeAdder : RiftLoader.instance.getListeners(DimensionTypeAdder.class)) {
            for (DimensionType dimensionType : dimensionTypeAdder.getDimensionTypes()) {
                dimensionTypes.put(dimensionType.getId(), dimensionType);
            }
        }
    }

    @Overwrite
    public static DimensionType getById(int id) {
        DimensionType dimensionType = dimensionTypes.get(id);
        if (dimensionType == null) {
            throw new IllegalArgumentException("Invalid dimension id " + id);
        }

        return dimensionType;
    }
}
