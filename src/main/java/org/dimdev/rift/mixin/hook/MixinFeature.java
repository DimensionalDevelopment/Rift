package org.dimdev.rift.mixin.hook;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import org.dimdev.rift.listener.StructureAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Feature.class)
public class MixinFeature {
    @Shadow @Final public static Map<String, Structure<?>> STRUCTURES;

    static {
        for (StructureAdder structureAdder : RiftLoader.instance.getListeners(StructureAdder.class)) {
            structureAdder.addStructuresToMap(STRUCTURES);
        }
    }
}
