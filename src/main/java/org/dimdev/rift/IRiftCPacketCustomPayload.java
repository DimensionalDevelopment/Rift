package org.dimdev.rift;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public interface IRiftCPacketCustomPayload {
    ResourceLocation getChannelName();
    PacketBuffer getData();
}
