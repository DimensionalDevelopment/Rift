package org.dimdev.testmod;

import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import org.dimdev.rift.injectedmethods.RiftFluid;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class WhiteFluid extends FlowingFluid implements RiftFluid {
    public WhiteFluid() {}

    @Override
    public Fluid getFlowingFluid() {
        return TestMod.FLOWING_WHITE_FLUID;
    }

    @Override
    public Fluid getStillFluid() {
        return TestMod.WHITE_FLUID;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public Item getFilledBucket() {
        return Items.WATER_BUCKET;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, IFluidState state, Random random) {}

    @Override @Nullable
    public IParticleData getDripParticleData() {
        return Particles.DRIPPING_WATER;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return true;
    }

    @Override
    protected void beforeReplacingBlock(IWorld world, BlockPos pos, IBlockState state) {
        state.dropBlockAsItem(world.getWorld(), pos, 0);
    }

    @Override
    public int getSlopeFindDistance(IWorldReaderBase worldIn) {
        return 4;
    }

    @Override
    public IBlockState getBlockState(IFluidState state) {
        return TestMod.BLOCK_WHITE_FLUID.getDefaultState().withProperty(BlockFlowingFluid.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isSameAs(Fluid fluid) {
        return fluid == TestMod.WHITE_FLUID || fluid == TestMod.FLOWING_WHITE_FLUID;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReaderBase world) {
        return 1;
    }

    @Override
    public int getTickRate(IWorldReaderBase world) {
        return 5;
    }

    @Override
    public boolean canOtherFlowInto(IFluidState state, Fluid fluid, EnumFacing direction) {
        return false;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public TextureAtlasSprite getStillTexture() {
        return Minecraft.getMinecraft().getModelManager().getBlockModelShapes().getModelForState(TestMod.BLOCK_WHITE_FLUID.getDefaultState()).getParticleTexture();
    }

    @Override
    public TextureAtlasSprite getFlowingTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getSprite(new ResourceLocation(TestMod.MODID, "block/white_fluid_flow"));
    }

    @Override
    public int getColorMultiplier(IWorldReader world, BlockPos pos) {
        int brightness = (int) MathHelper.clamp(255 * ((pos.getY() - 50) / 20.0), 0, 255);
        return (brightness << 16) + (brightness << 8) + brightness;
    }

    public static class Flowing extends WhiteFluid {
        public Flowing() {}

        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            builder.add(LEVEL_1_TO_8);
        }

        @Override
        public int getLevel(IFluidState getLevel) {
            return getLevel.getValue(LEVEL_1_TO_8);
        }

        @Override
        public boolean isSource(IFluidState state) {
            return false;
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
    }
}
