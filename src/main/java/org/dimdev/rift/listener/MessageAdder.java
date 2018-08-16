package org.dimdev.rift.listener;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.dimdev.rift.network.Message;

public interface MessageAdder {
    void registerMessages(RegistryNamespaced<ResourceLocation, Class<? extends Message>> registry);
}
