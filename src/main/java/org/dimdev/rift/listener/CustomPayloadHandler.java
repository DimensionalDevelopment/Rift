package org.dimdev.rift.listener;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * @deprecated Use {@link MessageAdder} instead.
 */
@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public interface CustomPayloadHandler {
    boolean clientHandlesChannel(ResourceLocation channelName);
    void clientHandleCustomPayload(ResourceLocation channelName, PacketBuffer bufferData);

    boolean serverHandlesChannel(ResourceLocation channelName);
    void serverHandleCustomPayload(ResourceLocation channelName, PacketBuffer bufferData);
}
