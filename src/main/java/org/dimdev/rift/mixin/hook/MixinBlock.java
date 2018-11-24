package org.dimdev.rift.mixin.hook;

import net.minecraft.block.Block;

import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.riftloader.RiftLoader;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Inject(method = "registerBlocks", at = @At(value = "FIELD", target = "Lnet/minecraft/util/registry/IRegistry;BLOCK:Lnet/minecraft/util/registry/IRegistry;", opcode = Opcodes.GETSTATIC, ordinal = 1))
    private static void onRegisterBlocks(CallbackInfo ci) {
        for (BlockAdder blockAdder : RiftLoader.instance.getListeners(BlockAdder.class)) {
            blockAdder.registerBlocks();
        }
    }
}
