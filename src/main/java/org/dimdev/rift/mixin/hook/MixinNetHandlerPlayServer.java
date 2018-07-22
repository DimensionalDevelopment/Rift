package org.dimdev.rift.mixin.hook;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.dimdev.rift.injectedmethods.RiftCPacketCustomPayload;
import org.dimdev.rift.listener.CustomPayloadHandler;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {
    @Inject(method = "processCustomPayload", at = @At("RETURN"))
    private void handleModCustomPayload(CPacketCustomPayload packet, CallbackInfo ci) {
        for (CustomPayloadHandler customPayloadHandler : RiftLoader.instance.getListeners(CustomPayloadHandler.class)) {
            if (customPayloadHandler.serverHandlesChannel(((RiftCPacketCustomPayload) packet).getChannelName())) {
                customPayloadHandler.serverHandleCustomPayload(((RiftCPacketCustomPayload) packet).getChannelName(), ((RiftCPacketCustomPayload) packet).getData());
            }
        }
    }
}
