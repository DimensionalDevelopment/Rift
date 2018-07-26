package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import org.dimdev.rift.listener.client.EntityRendererAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RenderManager.class)
public class MixinRenderManager {
    @Shadow @Final private Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(TextureManager textureManager, ItemRenderer itemRenderer, CallbackInfo ci) {
        for (EntityRendererAdder entityRendererAdder : RiftLoader.instance.getListeners(EntityRendererAdder.class)) {
            entityRendererAdder.addEntityRenderers(entityRenderMap, (RenderManager) (Object) this);
        }
    }
}
