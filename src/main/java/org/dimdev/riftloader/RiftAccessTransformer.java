package org.dimdev.riftloader;

import net.minecraft.launchwrapper.IClassTransformer;

public class RiftAccessTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return RiftLoader.instance.accessTransformer.transformClass(name, basicClass);
    }
}
