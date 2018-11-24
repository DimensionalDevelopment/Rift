package org.dimdev.rift.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

public class RegistryUtil {

    public static <T> IRegistry<T> createRegistry(ResourceLocation resourceLocation, IRegistry<T> registry) {
        IRegistry.REGISTRY.put(resourceLocation, registry);
        return registry;
    }

}
