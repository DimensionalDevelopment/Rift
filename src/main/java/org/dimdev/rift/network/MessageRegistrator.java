package org.dimdev.rift.network;

import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.BootstrapListener;
import org.dimdev.rift.listener.MessageAdder;
import org.dimdev.rift.network.message.MessageSpawnEntity;
import org.dimdev.riftloader.RiftLoader;

public class MessageRegistrator implements BootstrapListener {
    @Override
    public void afterVanillaBootstrap() {
        Message.REGISTRY.put(new ResourceLocation("rift:entity/spawn"), MessageSpawnEntity.class);
        for (MessageAdder messageAdder : RiftLoader.instance.getListeners(MessageAdder.class)) {
            messageAdder.registerMessages(Message.REGISTRY);
        }
    }
}
