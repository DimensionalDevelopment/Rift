package org.dimdev.rift;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class Rift implements InitializationListener {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void onInitialization() {
        LOGGER.info("Loading Rift!");

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.rift.core.json");
        Mixins.addConfiguration("mixins.rift.hooks.json");
    }
}
