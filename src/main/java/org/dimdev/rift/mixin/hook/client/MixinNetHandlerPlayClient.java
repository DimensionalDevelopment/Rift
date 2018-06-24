package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketCustomPayload;
import org.dimdev.rift.listener.CustomPayloadHandler;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    @Inject(method = "handleCustomPayload", at = @At("RETURN"))
    private void handleModCustomPayload(SPacketCustomPayload packet, CallbackInfo ci) {
        for (CustomPayloadHandler customPayloadHandler : SimpleLoader.instance.getListeners(CustomPayloadHandler.class)) {
            if (customPayloadHandler.clientHandlesChannel(packet.getChannelName())) {
                customPayloadHandler.clientHandleCustomPayload(packet.getChannelName(), packet.getBufferData());
            }
        }
    }
}
