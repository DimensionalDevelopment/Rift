package org.dimdev.rift.listener.client;

import net.minecraft.client.Minecraft;

public interface ClientTickable {
    void clientTick(Minecraft client);
}
