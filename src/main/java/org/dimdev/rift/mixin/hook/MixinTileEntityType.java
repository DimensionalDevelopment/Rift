package org.dimdev.rift.mixin.hook;

import net.minecraft.tileentity.TileEntityType;
import org.dimdev.rift.listener.TileEntityTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityType.class)
public abstract class MixinTileEntityType {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onRegisterTileEntityTypes(CallbackInfo ci) {
        for (TileEntityTypeAdder entityTypeAdder : RiftLoader.instance.getListeners(TileEntityTypeAdder.class)) {
            entityTypeAdder.registerTileEntityTypes();
        }
    }
}
