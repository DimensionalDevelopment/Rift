package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.dimdev.rift.listener.client.KeyBindingAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(GameSettings.class)
public class MixinGameSettings {
    @Shadow public KeyBinding[] keyBindings;

    @Inject(method = "loadOptions", at = @At("HEAD"))
    private void onLoadOptions(CallbackInfo ci) {
        List<KeyBinding> keyBindingList = new ArrayList<>(keyBindings.length);
        Collections.addAll(keyBindingList, keyBindings);
        for (KeyBindingAdder keyBindingAdder : RiftLoader.instance.getListeners(KeyBindingAdder.class)) {
            keyBindingList.addAll(keyBindingAdder.getKeyBindings());
        }
        keyBindings = keyBindingList.toArray(new KeyBinding[0]);
    }
}
