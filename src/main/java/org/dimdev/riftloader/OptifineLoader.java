package org.dimdev.riftloader;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.dimdev.rift.Rift.RiftTokens;
import org.dimdev.riftloader.listener.InitializationListener;

import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.List;

public class OptifineLoader implements InitializationListener {
	public static final String OPTIFINE_TRANSFORMER = "optifine.OptiFineClassTransformer";

    @Override
    public void onInitialization() {
        //noinspection unchecked
        ((List<String>) Launch.blackboard.get("TweakClasses")).add("org.dimdev.riftloader.OptifineLoader$Tweaker");
        Launch.classLoader.addClassLoaderExclusion("optifine.");
        RiftTokens.hasOptifine = true;
        Mixins.addConfiguration("mixins.rift.optifine.json");
    }

    public static class Tweaker implements ITweaker {
        @Override
        public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {}

        @Override
        public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        	if (RiftTokens.isObf) {
        		//Use Optifine's ClassTransformer when we're running in a normal (obfuscated) environment
        		classLoader.registerTransformer(OPTIFINE_TRANSFORMER);
        	} else {
        		//But if we're in a dev (deobfuscated) environment, we'll use our own
        		classLoader.registerTransformer("org.dimdev.riftloader.OptifineDevTransformer");
        	}
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
