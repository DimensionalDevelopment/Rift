package org.dimdev.rift.mixin.hook;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Shadow private static void registerBlock(ResourceLocation p_196249_0_, Block p_196249_1_) {}
    private static final BlockAdder.BlockRegistrationReceiver BLOCK_REGISTRATION_RECEIVER = MixinBlock::registerBlock;

    @Inject(method = "registerBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespacedDefaultedByKey;validateKey()V"))
    private static void onRegisterBlocks(CallbackInfo ci) {
        for (BlockAdder blockAdder : SimpleLoader.instance.getListeners(BlockAdder.class)) {
            blockAdder.registerBlocks(BLOCK_REGISTRATION_RECEIVER);
        }
    }
}
