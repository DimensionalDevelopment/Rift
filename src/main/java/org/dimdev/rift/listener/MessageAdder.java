package org.dimdev.rift.listener;

import net.minecraft.util.registry.IRegistry;
import org.dimdev.rift.network.Message;

public interface MessageAdder {
    void registerMessages(IRegistry<Class<? extends Message>> registry);
}
