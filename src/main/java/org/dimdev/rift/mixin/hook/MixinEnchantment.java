package org.dimdev.rift.mixin.hook;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.dimdev.rift.listener.EnchantmentAdder;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class MixinEnchantment {
    @Shadow @Final public static RegistryNamespaced<ResourceLocation, Enchantment> REGISTRY;

    @Inject(method = "registerEnchantments", at = @At(value = "RETURN"))
    private static void onRegisterEnchantments(CallbackInfo ci) {
        for (EnchantmentAdder enchantmentAdder : SimpleLoader.instance.getListeners(EnchantmentAdder.class)) {
            enchantmentAdder.registerEnchantments(REGISTRY::putObject);
        }
    }
}
