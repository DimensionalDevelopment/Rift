package org.dimdev.rift.listener;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public interface MobEffectAdder {
    interface MobEffectRegistrationReceiver {
        void registerMobEffect(ResourceLocation resourceLocation, Potion mobEffect);
    }

    void registerMobEffects(MobEffectRegistrationReceiver receiver);
}
