package org.dimdev.testmod;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CPacketTest implements Packet<INetHandlerPlayServer> {
    private static final Logger log = LogManager.getLogger();
    private int data;

    public CPacketTest() {}

    public CPacketTest(int data) {
        this.data = data;
    }

    @Override
    public void readPacketData(PacketBuffer buf) {
        data = buf.readVarInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeVarInt(data);
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        log.info("Server received test packet: " + data);
    }
}
