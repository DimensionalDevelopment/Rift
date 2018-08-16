package org.dimdev.rift.network;

import net.minecraft.network.NetworkManager;

public interface MessageContext {
    NetworkManager getNetworkManager();

    void reply(Message message);
}
