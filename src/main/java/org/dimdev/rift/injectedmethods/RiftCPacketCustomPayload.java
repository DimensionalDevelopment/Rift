package org.dimdev.rift.injectedmethods;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public interface RiftCPacketCustomPayload {
    ResourceLocation getChannelName();
    PacketBuffer getData();
}
