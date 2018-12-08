package org.dimdev.rift.mixin.hook;

import net.minecraft.world.biome.Biome;
import org.dimdev.rift.listener.BiomeAdder;
import org.dimdev.rift.listener.WorldChanger;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Biome.class)
public abstract class MixinBiome {
    @Inject(method = "registerBiomes", at = @At(value = "INVOKE", target = "Ljava/util/Collections;addAll(Ljava/util/Collection;[Ljava/lang/Object;)Z", remap = false))
    private static void onRegisterBiomes(CallbackInfo ci) {
        for (BiomeAdder biomeAdder : RiftLoader.instance.getListeners(BiomeAdder.class)) {
            biomeAdder.registerBiomes();
        }
    }

    @Inject(method = "register", at = @At(value = "RETURN"))
    private static void onRegisterBiome(int biomeId, String biomeName, Biome biome, CallbackInfo ci) {
        for (WorldChanger worldChanger : RiftLoader.instance.getListeners(WorldChanger.class)) {
            worldChanger.modifyBiome(biomeId, biomeName, biome);
        }
    }
}
