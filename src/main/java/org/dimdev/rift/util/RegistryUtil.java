package org.dimdev.rift.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

public class RegistryUtil {

    public static <T> IRegistry<T> createRegistry(ResourceLocation resourceLocation, IRegistry<T> registry) {
        IRegistry.field_212617_f.put(resourceLocation, registry);
        return registry;
    }

}
