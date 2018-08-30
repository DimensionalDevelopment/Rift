package org.dimdev.rift.listener;

import net.minecraft.block.Block;
import net.minecraft.item.ItemTool;

import java.util.Set;

public interface ToolEfficiencyProvider {
    void addEffectiveBlocks(ItemTool tool, Set<Block> target);
}
