package org.dimdev.rift.listener;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

public interface EnchantmentAdder {
    interface EnchantmentRegistrationReceiver {
        void registerEnchantment(ResourceLocation resourceLocation, Enchantment enchantment);
    }

    void registerEnchantments(EnchantmentRegistrationReceiver receiver);
}
