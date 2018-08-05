package org.dimdev.rift.mixin.hook;

import net.minecraft.world.gen.feature.structure.StructureIO;
import org.dimdev.rift.listener.StructureAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StructureIO.class)
public class MixinStructureIO {
    static {
        for (StructureAdder structureAdder : RiftLoader.instance.getListeners(StructureAdder.class)) {
            structureAdder.registerStructureNames();
        }
    }
}
