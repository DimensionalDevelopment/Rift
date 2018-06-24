package org.dimdev.rift.mixin.hook;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class MixinItem {
    @Shadow protected static void registerItemBlock(Block blockIn, Item itemIn) {}
    @Shadow private static void registerItemBlock(Block blockIn) {}
    @Shadow private static void registerItemBlock(Block p_200879_0_, ItemGroup p_200879_1_) {}

    private static final ItemAdder.ItemRegistrationReceiver ITEM_REGISTRATION_RECEIVER = new ItemAdder.ItemRegistrationReceiver() {
        @Override
        public void registerItem(ResourceLocation resourceLocation, Item item) {
            Item.REGISTRY.putObject(resourceLocation, item);
        }

        @Override
        public void registerItemBlock(Block block) {
            MixinItem.registerItemBlock(block);
        }

        @Override
        public void registerItemBlock(Block block, ItemGroup itemGroup) {
            MixinItem.registerItemBlock(block, itemGroup);
        }

        @Override
        public void registerItemBlock(Block block, Item item) {
            MixinItem.registerItemBlock(block, item);
        }
    };

    @Inject(method = "registerItems", at = @At("RETURN"))
    private static void onRegisterItems(CallbackInfo ci) {
        for (ItemAdder itemAdder : SimpleLoader.instance.getListeners(ItemAdder.class)) {
            itemAdder.registerItems(ITEM_REGISTRATION_RECEIVER);
        }
    }
}
