package org.dimdev.rift.listener;

import net.minecraft.server.MinecraftServer;

public interface ServerTickable {
    void serverTick(MinecraftServer server);
}
