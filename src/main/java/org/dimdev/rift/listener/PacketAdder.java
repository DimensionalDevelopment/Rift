package org.dimdev.rift.listener;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;

public interface PacketAdder {
    interface PacketRegistrationReceiver {
        EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet<?>> packetClass);
    }

    void registerHandshakingPackets(PacketRegistrationReceiver receiver);
    void registerPlayPackets(PacketRegistrationReceiver receiver);
    void registerStatusPackets(PacketRegistrationReceiver receiver);
    void registerLoginPackets(PacketRegistrationReceiver receiver);
}
