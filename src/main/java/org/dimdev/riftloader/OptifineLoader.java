package org.dimdev.riftloader;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.dimdev.riftloader.listener.InitializationListener;

import java.io.File;
import java.util.List;

public class OptifineLoader implements InitializationListener {
    @Override
    public void onInitialization() {
        //noinspection unchecked
        ((List<String>) Launch.blackboard.get("TweakClasses")).add("org.dimdev.riftloader.OptifineLoader$Tweaker");
        Launch.classLoader.addClassLoaderExclusion("optifine.");
    }

    public static class Tweaker implements ITweaker {
        @Override
        public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {}

        @Override
        public void injectIntoClassLoader(LaunchClassLoader classLoader) {
            classLoader.registerTransformer("optifine.OptiFineClassTransformer");
        }

        @Override
        public String getLaunchTarget() {
            return null;
        }

        @Override
        public String[] getLaunchArguments() {
            return new String[0];
        }
    }
}
