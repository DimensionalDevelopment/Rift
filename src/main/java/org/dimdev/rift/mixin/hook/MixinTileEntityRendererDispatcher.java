package org.dimdev.rift.mixin.hook;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.dimdev.rift.listener.TileEntityRendererAdder;
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

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    private void onRegisterTileEntityRenderDispatcher(CallbackInfo ci) {
        for (TileEntityRendererAdder tileEntityRendererAdder : RiftLoader.instance.getListeners(TileEntityRendererAdder.class)) {
            tileEntityRendererAdder.addRenderers(renderers);
        }
    }
}
