package org.dimdev.rift.mixin.hook;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import org.dimdev.rift.listener.BiomeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(OverworldBiomeProvider.class)
public abstract class MixinOverworldBiomeProvider {
    @Shadow @Final @Mutable private Biome[] biomes;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        List<Biome> biomes = new ArrayList<>(Arrays.asList(this.biomes));
        for (BiomeAdder biomeAdder : RiftLoader.instance.getListeners(BiomeAdder.class)) {
            biomes.addAll(biomeAdder.getOverworldBiomes());
        }

        this.biomes = biomes.toArray(new Biome[0]);
    }
}
