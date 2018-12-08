package org.dimdev.rift;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IEnvironmentTokenProvider;

import net.minecraft.launchwrapper.Launch;

public class Rift implements InitializationListener {
	public static final int DATAFIXER_VERSION = 1631;
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialization() {
        LOGGER.info("Loading Rift!");

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.rift.core.json");
        Mixins.addConfiguration("mixins.rift.hooks.json");
        MixinEnvironment.getDefaultEnvironment().registerTokenProviderClass("org.dimdev.rift.Rift$RiftTokens");
    }

    public static class RiftTokens implements IEnvironmentTokenProvider {
    	/** Whether we are running with obfuscated Notch names or deobfuscated MCP names */
    	public static boolean isObf = isObf();
    	/** Whether Optifine is present and has probably been loaded */
    	public static boolean hasOptifine = false;

    	private static boolean isObf() {
    		try {
    			return Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
    		} catch (IOException e) {
    			return true; //We probably are if we can't find World
    		}
    	}

		@Override
		public int getPriority() {
			return DEFAULT_PRIORITY;
		}

		@Override
		public Integer getToken(String token, MixinEnvironment env) {
			switch (token) {
			case "OBF":
				return isObf ? 1 : 0;

			case "OPTIFINE":
				return hasOptifine ? 2 : 0;

			default:
				return null;
			}
		}
    }
}
