package org.dimdev.rift;

import org.dimdev.simpleloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class Rift implements InitializationListener {
    @Override
    public void onInitialization() {
        System.out.println("Loading Rift!");

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.rift.core.json");
        Mixins.addConfiguration("mixins.rift.hooks.json");
    }
}
