package org.dimdev.rift;

import org.dimdev.rift.Rift.RiftTokens;
import org.dimdev.riftloader.listener.InitializationListener;

import org.spongepowered.asm.mixin.Mixins;

public class LateRift implements InitializationListener {

	@Override
	public void onInitialization() {
		if (!RiftTokens.hasOptifine) {
			Mixins.addConfiguration("mixins.rift.no_optifine.json");
		}
	}
}