package org.dimdev.rift.listener;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public interface CustomPayloadHandler {
    boolean clientHandlesChannel(ResourceLocation channelName);
    void clientHandleCustomPayload(ResourceLocation channelName, PacketBuffer bufferData);

    boolean serverHandlesChannel(String channelName);
    void serverHandleCustomPayload(String channelName, PacketBuffer bufferData);
}
