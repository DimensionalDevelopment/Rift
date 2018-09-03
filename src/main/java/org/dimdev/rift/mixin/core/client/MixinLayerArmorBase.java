package org.dimdev.rift.mixin.core.client;

import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase<T extends ModelBase> {
    @Redirect(
        method = "getArmorResource(Lnet/minecraft/item/ItemArmor;ZLjava/lang/String;)Lnet/minecraft/util/ResourceLocation;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"
        )
    )
    private String getArmorTexture(String format, Object... args) {
        String name = (String) args[0];
        int layer = (int) args[1];
        String typeSuffix = (String) args[2];
        ResourceLocation location = new ResourceLocation(name);
        return String.format("%s:textures/models/armor/%s_layer_%d%s.png", location.getNamespace(), location.getPath(), layer, typeSuffix);
    }
}
