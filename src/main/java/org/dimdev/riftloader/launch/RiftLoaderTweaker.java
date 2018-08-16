package org.dimdev.riftloader.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.List;

public abstract class RiftLoaderTweaker implements ITweaker {
    public List<String> args;

    protected void addArg(String name, String value) {
        args.add(name);
        if (value != null) {
            args.add(value);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        // Use the Launch classLoader to load the RiftLoader class. Otherwise identical
        // classes may not be equal, and 'instanceof' may return false when it should be true.
        try {
            Class<?> clazz = Launch.classLoader.findClass("org.dimdev.riftloader.RiftLoader");
            clazz.getMethod("load", boolean.class).invoke(clazz.getField("instance").get(null), isClient());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        MixinEnvironment.getDefaultEnvironment().setSide(isClient() ? MixinEnvironment.Side.CLIENT : MixinEnvironment.Side.SERVER);
    }

    protected abstract boolean isClient();

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[0]);
    }
}
