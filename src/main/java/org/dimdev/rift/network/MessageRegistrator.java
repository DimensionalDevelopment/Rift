package org.dimdev.rift.network;

import org.dimdev.rift.listener.MessageAdder;
import org.dimdev.rift.listener.MinecraftStartListener;
import org.dimdev.riftloader.RiftLoader;

public class MessageRegistrator implements MinecraftStartListener {
    @Override
    public void onMinecraftStart() {
        for (MessageAdder messageAdder : RiftLoader.instance.getListeners(MessageAdder.class)) {
            messageAdder.registerMessages(Message.REGISTRY);
        }
    }
}
