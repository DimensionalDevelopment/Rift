package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.client.TextureAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ModelBakery.class)
public class MixinModelBakery {
    @Shadow @Final private static Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES;

    static {
        for (TextureAdder textureAdder : RiftLoader.instance.getListeners(TextureAdder.class)) {
            LOCATIONS_BUILTIN_TEXTURES.addAll(textureAdder.getBuiltinTextures());
        }
    }
}
