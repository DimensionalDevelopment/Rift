package org.dimdev.rift.mixin.hook;

import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import org.dimdev.rift.listener.BiomeAdder;
import org.dimdev.rift.listener.EntityTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityType.class)
public abstract class MixinEntityType {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onRegisterEntityTypes(CallbackInfo ci) {
        for (EntityTypeAdder entityTypeAdder : RiftLoader.instance.getListeners(EntityTypeAdder.class)) {
            entityTypeAdder.registerEntityTypes();
        }
    }
}
