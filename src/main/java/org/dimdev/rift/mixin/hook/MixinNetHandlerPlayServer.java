package org.dimdev.rift.mixin.hook;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.injectedmethods.RiftCPacketCustomPayload;
import org.dimdev.rift.listener.CustomPayloadHandler;
import org.dimdev.rift.network.Message;
import org.dimdev.rift.network.ServerMessageContext;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {
    @Shadow @Final private MinecraftServer server;
    @Shadow public EntityPlayerMP player;
    @Shadow @Final public NetworkManager netManager;

    @SuppressWarnings("deprecation")
    @Inject(method = "processCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleModCustomPayload(CPacketCustomPayload packet, CallbackInfo ci) {
        ResourceLocation channelName = ((RiftCPacketCustomPayload) packet).getChannelName();
        PacketBuffer data = ((RiftCPacketCustomPayload) packet).getData();

        for (CustomPayloadHandler customPayloadHandler : RiftLoader.instance.getListeners(CustomPayloadHandler.class)) {
            if (customPayloadHandler.serverHandlesChannel(channelName)) {
                customPayloadHandler.serverHandleCustomPayload(channelName, data);
            }
        }

        Class<? extends Message> messageClass = Message.REGISTRY.get(channelName);
        if (messageClass != null) {
            try {
                Message message = RiftLoader.instance.newInstance(messageClass);
                message.read(data);
                message.process(new ServerMessageContext(server, player, netManager));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Error creating " + messageClass, e);
            }
            ci.cancel();
        }
    }
}
