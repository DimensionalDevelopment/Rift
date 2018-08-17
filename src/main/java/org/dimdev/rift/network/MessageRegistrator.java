package org.dimdev.rift.network;

import org.dimdev.rift.listener.BootstrapListener;
import org.dimdev.rift.listener.MessageAdder;
import org.dimdev.riftloader.RiftLoader;

public class MessageRegistrator implements BootstrapListener {
    @Override
    public void afterVanillaBootstrap() {
        for (MessageAdder messageAdder : RiftLoader.instance.getListeners(MessageAdder.class)) {
            messageAdder.registerMessages(Message.REGISTRY);
        }
    }
}
