package org.dimdev.testmod;

import net.minecraft.network.PacketBuffer;
import org.dimdev.rift.network.ClientMessageContext;
import org.dimdev.rift.network.Message;
import org.dimdev.rift.network.ServerMessageContext;

public class TestMessage extends Message {
    public int data;

    public TestMessage() {}

    public TestMessage(int data) {
        this.data = data;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(data);
    }

    @Override
    public void read(PacketBuffer buffer) {
        data = buffer.readVarInt();
    }

    @Override
    public void process(ClientMessageContext context) {
        System.out.println("Received message on client: " + data);

        if (Math.random() < 0.5) {
            context.reply(new TestMessage(data + 1));
        }
    }

    @Override
    public void process(ServerMessageContext context) {
        System.out.println("Received message on server: " + data);

        if (Math.random() < 0.5) {
            context.reply(new TestMessage(data + 1));
        }
    }
}
