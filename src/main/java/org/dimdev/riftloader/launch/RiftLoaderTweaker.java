package org.dimdev.riftloader.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RiftLoaderTweaker implements ITweaker {
    public List<String> args;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = new ArrayList<>(args);

        addArg("--version", profile);
        addArg("--assetsDir", assetsDir.getPath());
    }

    private void addArg(String name, String value) {
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
            clazz.getMethod("load").invoke(clazz.getField("instance").get(null));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[0]);
    }
}
