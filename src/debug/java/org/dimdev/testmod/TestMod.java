package org.dimdev.testmod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.rift.listener.FluidAdder;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.rift.listener.TextureAdder;

import java.util.Arrays;
import java.util.Collection;

public class TestMod implements BlockAdder, ItemAdder, FluidAdder, TextureAdder {
    public static final Block WHITE_BLOCK = new Block(Block.Builder.create(Material.ROCK));
    public static final Block TRANSLUCENT_WHITE_BLOCK = new BlockStainedGlass(EnumDyeColor.WHITE, Block.Builder.create(Material.GLASS));
    public static final FlowingFluid WHITE_FLUID = new WhiteFluid.Source();
    public static final FlowingFluid FLOWING_WHITE_FLUID = new WhiteFluid.Flowing();
    public static final BlockFlowingFluid BLOCK_WHITE_FLUID = new BlockFlowingFluid(WHITE_FLUID, Block.Builder.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100F, 100F)) {
        @Override
        public boolean canCacheOpacity() {
            return true;
        }
    };

    @Override
    public void registerBlocks(BlockRegistrationReceiver receiver) {
        receiver.registerBlock(new ResourceLocation("assets/testmod", "white_block"), WHITE_BLOCK);
        receiver.registerBlock(new ResourceLocation("assets/testmod", "translucent_white_block"), TRANSLUCENT_WHITE_BLOCK);
        receiver.registerBlock(new ResourceLocation("assets/testmod", "white_fluid"), BLOCK_WHITE_FLUID);
    }

    @Override
    public void registerItems(ItemRegistrationReceiver receiver) {
        receiver.registerItemBlock(WHITE_BLOCK, ItemGroup.BUILDING_BLOCKS);
        receiver.registerItemBlock(TRANSLUCENT_WHITE_BLOCK, ItemGroup.BUILDING_BLOCKS);
    }

    @Override
    public void registerFluids(FluidRegistrationReceiver receiver) {
        receiver.registerFluid(new ResourceLocation("assets/testmod", "white_fluid"), WHITE_FLUID);
        receiver.registerFluid(new ResourceLocation("assets/testmod", "flowing_white_fluid"), FLOWING_WHITE_FLUID);
    }

    @Override
    public Collection<? extends ResourceLocation> getBuiltinTextures() {
        return Arrays.asList(new ResourceLocation("assets/testmod", "blocks/white_fluid_still"), new ResourceLocation("assets/testmod", "blocks/white_fluid_flow"));
    }
}
