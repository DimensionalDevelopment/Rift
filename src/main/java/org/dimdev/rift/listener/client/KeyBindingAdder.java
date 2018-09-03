package org.dimdev.rift.listener.client;

import net.minecraft.client.settings.KeyBinding;

import java.util.Collection;

public interface KeyBindingAdder {
    Collection<? extends KeyBinding> getKeyBindings();
}
