package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.dimdev.rift.listener.client.TileEntityRendererAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {
    @Shadow @Final private Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onRegisterTileEntityRenderDispatcher(CallbackInfo ci) {
        for (TileEntityRendererAdder tileEntityRendererAdder : RiftLoader.instance.getListeners(TileEntityRendererAdder.class)) {
            tileEntityRendererAdder.addTileEntityRenderers(renderers);
        }

        for (TileEntityRenderer renderer : renderers.values()) {
            renderer.setRendererDispatcher((TileEntityRendererDispatcher) (Object) this);
        }
    }
}
