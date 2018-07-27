package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.gui.GuiIngame;
import org.dimdev.rift.listener.client.OverlayRenderer;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void onRenderGameOverlay(CallbackInfo ci) {
        for (OverlayRenderer overlayRenderer : RiftLoader.instance.getListeners(OverlayRenderer.class)) {
            overlayRenderer.renderOverlay();
        }
    }
}
