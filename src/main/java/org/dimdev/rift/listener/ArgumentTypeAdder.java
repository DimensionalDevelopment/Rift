package org.dimdev.rift.listener;

import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.util.ResourceLocation;

public interface ArgumentTypeAdder {
    /**
     * Use {@link net.minecraft.command.arguments.ArgumentTypes#register(ResourceLocation, Class, IArgumentSerializer)}
     * to register argument types.
     */
    void addArgumentTypes();
}
