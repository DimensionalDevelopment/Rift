package org.dimdev.testmod;

import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import org.dimdev.rift.IRiftFluid;

public abstract class WhiteFluid extends FlowingFluid implements IRiftFluid {
    public WhiteFluid() {}

    @Override
    public IFluidState func_207207_a(int p_207207_1_, boolean p_207207_2_) {
        return TestMod.FLOWING_WHITE_FLUID.getDefaultState().withProperty(field_207210_b, p_207207_1_).withProperty(field_207209_a, p_207207_2_);
    }

    @Override
    public IFluidState func_207204_a(boolean p_207204_1_) {
        return TestMod.WHITE_FLUID.getDefaultState().withProperty(field_207209_a, p_207204_1_);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getFilledBucket() {
        return Items.WATER_BUCKET;
    }

    @Override
    protected boolean func_205579_d() {
        return true;
    }

    @Override
    protected void func_205580_a(IWorld p_205580_1_, BlockPos p_205580_2_, IBlockState p_205580_3_) {
        p_205580_3_.spawnItem(p_205580_1_.getWorld(), p_205580_2_, 0);
    }

    @Override
    public int getSlopeFindDistance(IWorldReaderBase worldIn) {
        return 4;
    }

    @Override
    public IBlockState getBlockState(IFluidState state) {
        return TestMod.BLOCK_WHITE_FLUID.getDefaultState().withProperty(BlockFlowingFluid.LEVEL, func_207205_e(state));
    }

    @Override
    public boolean isSameAs(Fluid fluid) {
        return fluid == TestMod.WHITE_FLUID || fluid == TestMod.FLOWING_WHITE_FLUID;
    }

    @Override
    public int getViscosity(IWorldReaderBase world) {
        return 1;
    }

    @Override
    public int getSpread(IWorldReaderBase world) {
        return 5;
    }

    @Override
    public TextureAtlasSprite getStillTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().func_195424_a(new ResourceLocation("testmod", "blocks/white_fluid_still"));
    }

    @Override
    public TextureAtlasSprite getFlowingTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().func_195424_a(new ResourceLocation("testmod", "blocks/white_fluid_flow"));
    }

    @Override
    public int getColorMultiplier() {
        return 0xFFFFFF;
    }

    public static class Flowing extends WhiteFluid {
        public Flowing() {}

        @Override
        protected void buildStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.buildStateContainer(builder);
            builder.func_206894_a(field_207210_b);
        }

        @Override
        public int getLevel(IFluidState getLevel) {
            return getLevel.getValue(field_207210_b);
        }

        @Override
        public boolean isSource(IFluidState state) {
            return false;
        }

        @Override
        public boolean func_204635_a(Fluid p_204635_1_, EnumFacing p_204635_2_) {
            return p_204635_2_ == EnumFacing.DOWN;
        }
    }

    public static class Source extends WhiteFluid {
        public Source() {}

        @Override
        public int getLevel(IFluidState getLevel) {
            return 8;
        }

        @Override
        public boolean isSource(IFluidState state) {
            return true;
        }

        @Override
        public boolean func_204635_a(Fluid p_204635_1_, EnumFacing p_204635_2_) {
            return p_204635_2_ == EnumFacing.DOWN && p_204635_1_ != TestMod.FLOWING_WHITE_FLUID;
        }
    }
}
