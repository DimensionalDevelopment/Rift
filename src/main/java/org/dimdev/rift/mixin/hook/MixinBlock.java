package org.dimdev.rift.mixin.hook;

import net.minecraft.block.Block;
import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Inject(method = "registerBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespacedDefaultedByKey;validateKey()V"))
    private static void onRegisterBlocks(CallbackInfo ci) {
        for (BlockAdder blockAdder : RiftLoader.instance.getListeners(BlockAdder.class)) {
            blockAdder.registerBlocks();
        }
    }
}
