package org.dimdev.rift.listener;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public interface ItemAdder {
    interface ItemRegistrationReceiver {
        void registerItem(ResourceLocation resourceLocation, Item item);
        void registerItemBlock(Block block);
        void registerItemBlock(Block block, ItemGroup itemGroup);
        void registerItemBlock(Block block, Item item);
    }

    void registerItems(ItemRegistrationReceiver receiver);
}
