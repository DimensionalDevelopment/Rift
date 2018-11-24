package org.dimdev.rift.network;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.WorldServer;
import org.dimdev.rift.util.RegistryUtil;

import java.util.function.Predicate;

public abstract class Message {
    public static final IRegistry<Class<? extends Message>> REGISTRY = RegistryUtil.createRegistry(new ResourceLocation("rift", "message"), new RegistryNamespaced<>());

    public abstract void write(PacketBuffer buffer);

    public abstract void read(PacketBuffer buffer);

    public void process(ClientMessageContext context) {
        throw new UnsupportedOperationException("Packet " + getClass() + " can't be processed on client.");
    }

    public void process(ServerMessageContext context) {
        throw new UnsupportedOperationException("Packet " + getClass() + " can't be processed on server.");
    }

    public final Packet<? extends INetHandler> toPacket(EnumPacketDirection direction) {
        ResourceLocation id = Message.REGISTRY.getKey(getClass());
        if (id == null) {
            throw new IllegalArgumentException("Message was not registered: " + this);
        }

        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        write(buffer);
        switch (direction) {
            case CLIENTBOUND:
                return new SPacketCustomPayload(id, buffer);
            case SERVERBOUND:
                return new CPacketCustomPayload(id, buffer);
            default:
                throw new AssertionError("unreachable");
        }
    }

    public final void send(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).connection.getNetworkManager().sendPacket(toPacket(EnumPacketDirection.CLIENTBOUND));
        } else if (player instanceof EntityPlayerSP) {
            ((EntityPlayerSP) player).connection.getNetworkManager().sendPacket(toPacket(EnumPacketDirection.SERVERBOUND));
        } else {
            throw new IllegalArgumentException("Only supported for EntityPlayerMP and EntityPlayerSP, but got " + player.getClass());
        }
    }

    public final void sendToAll(MinecraftServer server) {
        for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
            send(player);
        }
    }

    public final void sendToAll(MinecraftServer server, Predicate<EntityPlayerMP> filter) {
        for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
            if (filter.test(player)) {
                send(player);
            }
        }
    }

    public final void sendToAll(WorldServer world, Predicate<EntityPlayerMP> filter) {
        for (EntityPlayerMP player : world.getPlayers(EntityPlayerMP.class, filter)) {
            send(player);
        }
    }

    public final void sendToAll(WorldServer world) {
        for (EntityPlayerMP player : world.getPlayers(EntityPlayerMP.class, player -> true)) {
            send(player);
        }
    }

    public void sendToServer() {
        Minecraft.getInstance().player.connection.sendPacket(toPacket(EnumPacketDirection.SERVERBOUND));
    }
}
