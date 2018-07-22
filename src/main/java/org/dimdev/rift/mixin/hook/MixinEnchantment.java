package org.dimdev.rift.mixin.hook;

import net.minecraft.enchantment.Enchantment;
import org.dimdev.rift.listener.EnchantmentAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class MixinEnchantment {
    @Inject(method = "registerEnchantments", at = @At(value = "RETURN"))
    private static void onRegisterEnchantments(CallbackInfo ci) {
        for (EnchantmentAdder enchantmentAdder : RiftLoader.instance.getListeners(EnchantmentAdder.class)) {
            enchantmentAdder.registerEnchantments();
        }
    }
}
