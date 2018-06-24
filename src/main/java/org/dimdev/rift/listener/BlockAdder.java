package org.dimdev.rift.listener;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public interface BlockAdder {
    interface BlockRegistrationReceiver {
        void registerBlock(ResourceLocation location, Block block);
    }

    void registerBlocks(BlockRegistrationReceiver receiver);
}
