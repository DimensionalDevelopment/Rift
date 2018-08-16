package org.dimdev.rift.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;

public class ServerMessageContext implements MessageContext {
    private final MinecraftServer server;
    private final EntityPlayerMP sender;
    private final NetworkManager networkManager;

    public ServerMessageContext(MinecraftServer server, EntityPlayerMP sender, NetworkManager networkManager) {
        this.server = server;
        this.sender = sender;
        this.networkManager = networkManager;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public EntityPlayerMP getSender() {
        return sender;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public void reply(Message message) {
        message.send(sender);
    }
}
