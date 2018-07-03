package org.dimdev.rift.mixin.hook;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.dimdev.rift.IRiftCPacketCustomPayload;
import org.dimdev.rift.listener.CustomPayloadHandler;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {
    @Inject(method = "processCustomPayload", at = @At("RETURN"))
    private void handleModCustomPayload(CPacketCustomPayload packet, CallbackInfo ci) {
        for (CustomPayloadHandler customPayloadHandler : SimpleLoader.instance.getListeners(CustomPayloadHandler.class)) {
            if (customPayloadHandler.serverHandlesChannel(((IRiftCPacketCustomPayload) packet).getChannelName())) {
                customPayloadHandler.serverHandleCustomPayload(((IRiftCPacketCustomPayload) packet).getChannelName(), ((IRiftCPacketCustomPayload) packet).getData());
            }
        }
    }
}
