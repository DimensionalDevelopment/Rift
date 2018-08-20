package org.dimdev.rift.listener;

import net.minecraft.server.MinecraftServer;

@ListenerInterface
public interface ServerTickable {
    void serverTick(MinecraftServer server);
}
