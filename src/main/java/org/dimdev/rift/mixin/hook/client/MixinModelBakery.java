package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.TextureAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ModelBakery.class)
public class MixinModelBakery {
    @Shadow @Final private static Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES;
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void addBuiltinTextures(CallbackInfo ci) {
        for (TextureAdder textureAdder : RiftLoader.instance.getListeners(TextureAdder.class)) {
            LOCATIONS_BUILTIN_TEXTURES.addAll(textureAdder.getBuiltinTextures());
        }
    }
}
