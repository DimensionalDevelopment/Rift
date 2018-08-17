package org.dimdev.rift.mixin.core.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldReaderBase;
import org.dimdev.rift.injectedmethods.RiftFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockFluidRenderer.class)
public abstract class MixinBlockFluidRenderer {
    @Shadow private static boolean isAdjacentFluidSameAs(IBlockReader world, BlockPos pos, EnumFacing side, IFluidState state) { return false; }
    @Shadow private static boolean func_209556_a(IBlockReader p_209556_0_, BlockPos p_209556_1_, EnumFacing p_209556_2_, float p_209556_3_) { return false; }
    @Shadow protected abstract float func_204504_a(IWorldReaderBase p_204504_1_, BlockPos p_204504_2_, Fluid p_204504_3_);
    @Shadow protected abstract int func_204835_a(IWorldReader p_204835_1_, BlockPos p_204835_2_);
    @Shadow private TextureAtlasSprite atlasSpriteWaterOverlay;

    @Overwrite
    public boolean render(IWorldReader world, BlockPos pos, BufferBuilder buffer, IFluidState state) {
        boolean isLava = state.isTagged(FluidTags.LAVA);

        // Get textures and color multiplier
        TextureAtlasSprite stillTexture = MissingTextureSprite.getSprite();
        TextureAtlasSprite flowingTexture = MissingTextureSprite.getSprite();
        int colorMultiplier = 0xFFFFFF;
        if (state.getFluid() instanceof RiftFluid) {
            RiftFluid fluid = (RiftFluid) state.getFluid();
            stillTexture = fluid.getStillTexture();
            flowingTexture = fluid.getFlowingTexture();
            colorMultiplier = fluid.getColorMultiplier(world, pos);
        }

        float redMultiplier = (colorMultiplier >> 16 & 255) / 255F;
        float greenMultiplier = (colorMultiplier >> 8 & 255) / 255F;
        float blueMultiplier = (colorMultiplier & 255) / 255F;

        boolean renderTop = !isAdjacentFluidSameAs(world, pos, EnumFacing.UP, state);
        boolean renderBottom = !isAdjacentFluidSameAs(world, pos, EnumFacing.DOWN, state) && !func_209556_a(world, pos, EnumFacing.DOWN, 0.8888889F);
        boolean renderNorth = !isAdjacentFluidSameAs(world, pos, EnumFacing.NORTH, state);
        boolean renderSouth = !isAdjacentFluidSameAs(world, pos, EnumFacing.SOUTH, state);
        boolean renderWest = !isAdjacentFluidSameAs(world, pos, EnumFacing.WEST, state);
        boolean renderEast = !isAdjacentFluidSameAs(world, pos, EnumFacing.EAST, state);

        if (!renderTop && !renderBottom && !renderEast && !renderWest && !renderNorth && !renderSouth) {
            return false;
        }

        boolean rendered = false;

        float var17 = func_204504_a(world, pos, state.getFluid());
        float var18 = func_204504_a(world, pos.south(), state.getFluid());
        float var19 = func_204504_a(world, pos.east().south(), state.getFluid());
        float var20 = func_204504_a(world, pos.east(), state.getFluid());

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        float minU;
        float maxU;
        float minV;
        float maxV;
        float var34;

        if (renderTop && !func_209556_a(world, pos, EnumFacing.UP, Math.min(Math.min(var17, var18), Math.min(var19, var20)))) {
            rendered = true;
            var17 -= 0.001F;
            var18 -= 0.001F;
            var19 -= 0.001F;
            var20 -= 0.001F;
            Vec3d var25 = state.getFlow(world, pos);
            float var28;
            float var30;
            float var32;
            TextureAtlasSprite texture;
            float var37;

            if (var25.x == 0 && var25.z == 0) {
                texture = stillTexture;
                minU = texture.getInterpolatedU(0);
                var28 = texture.getInterpolatedV(0);
                maxU = minU;
                var30 = texture.getInterpolatedV(16);
                minV = texture.getInterpolatedU(16);
                var32 = var30;
                maxV = minV;
                var34 = var28;
            } else {
                texture = flowingTexture;
                float var35 = (float) MathHelper.atan2(var25.z, var25.x) - 1.5707964F;
                float var36 = MathHelper.sin(var35) * 0.25F;
                var37 = MathHelper.cos(var35) * 0.25F;
                minU = texture.getInterpolatedU(8 + (-var37 - var36) * 16);
                var28 = texture.getInterpolatedV(8 + (-var37 + var36) * 16);
                maxU = texture.getInterpolatedU(8 + (-var37 + var36) * 16);
                var30 = texture.getInterpolatedV(8 + (var37 + var36) * 16);
                minV = texture.getInterpolatedU(8 + (var37 + var36) * 16);
                var32 = texture.getInterpolatedV(8 + (var37 - var36) * 16);
                maxV = texture.getInterpolatedU(8 + (var37 - var36) * 16);
                var34 = texture.getInterpolatedV(8 + (-var37 - var36) * 16);
            }

            int blockLight = func_204835_a(world, pos);
            int skyLight = blockLight >> 16;

            buffer.pos(x, y + var17, z).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(minU, var28).lightmap(skyLight, blockLight).endVertex();
            buffer.pos(x, y + var18, z + 1).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(maxU, var30).lightmap(skyLight, blockLight).endVertex();
            buffer.pos(x + 1, y + var19, z + 1).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(minV, var32).lightmap(skyLight, blockLight).endVertex();
            buffer.pos(x + 1, y + var20, z).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(maxV, var34).lightmap(skyLight, blockLight).endVertex();
            if (state.shouldRenderSides(world, pos.up())) {
                buffer.pos(x, y + var17, z).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(minU, var28).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(x + 1, y + var20, z).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(maxV, var34).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(x + 1, y + var19, z + 1).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(minV, var32).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(x, y + var18, z + 1).color(redMultiplier, greenMultiplier, blueMultiplier, 1).tex(maxU, var30).lightmap(skyLight, blockLight).endVertex();
            }
        }

        if (renderBottom) {
            minU = stillTexture.getMinU();
            maxU = stillTexture.getMaxU();
            minV = stillTexture.getMinV();
            maxV = stillTexture.getMaxV();

            int blockLight = func_204835_a(world, pos.down());
            int skyLight = blockLight >> 16;

            float darkerRedMultiplier = 0.5F * redMultiplier;
            float darkerGreenMultiplier = 0.5F * greenMultiplier;
            float darkerBlueMultiplier = 0.5F * blueMultiplier;

            buffer.pos(x, y, z + 1).color(darkerRedMultiplier, darkerGreenMultiplier, darkerBlueMultiplier, 1).tex(minU, maxV).lightmap(skyLight, blockLight).endVertex();
            buffer.pos(x, y, z).color(darkerRedMultiplier, darkerGreenMultiplier, darkerBlueMultiplier, 1).tex(minU, minV).lightmap(skyLight, blockLight).endVertex();
            buffer.pos(x + 1, y, z).color(darkerRedMultiplier, darkerGreenMultiplier, darkerBlueMultiplier, 1).tex(maxU, minV).lightmap(skyLight, blockLight).endVertex();
            buffer.pos(x + 1, y, z + 1).color(darkerRedMultiplier, darkerGreenMultiplier, darkerBlueMultiplier, 1).tex(maxU, maxV).lightmap(skyLight, blockLight).endVertex();

            rendered = true;
        }

        for (int direction = 0; direction < 4; ++direction) {
            double x1;
            double z1;
            double x2;
            double z2;
            EnumFacing side;
            boolean render;
            if (direction == 0) {
                maxU = var17;
                minV = var20;
                x1 = x;
                x2 = x + 1;
                z1 = z + 0.001;
                z2 = z + 0.001;
                side = EnumFacing.NORTH;
                render = renderNorth;
            } else if (direction == 1) {
                maxU = var19;
                minV = var18;
                x1 = x + 1;
                x2 = x;
                z1 = z + 1 - 0.001;
                z2 = z + 1 - 0.001;
                side = EnumFacing.SOUTH;
                render = renderSouth;
            } else if (direction == 2) {
                maxU = var18;
                minV = var17;
                x1 = x + 0.001;
                x2 = x + 0.001;
                z1 = z + 1;
                z2 = z;
                side = EnumFacing.WEST;
                render = renderWest;
            } else {
                maxU = var20;
                minV = var19;
                x1 = x + 1 - 0.001;
                x2 = x + 1 - 0.001;
                z1 = z;
                z2 = z + 1;
                side = EnumFacing.EAST;
                render = renderEast;
            }

            if (render && !func_209556_a(world, pos, side, Math.max(maxU, minV))) {
                rendered = true;
                BlockPos var55 = pos.offset(side);
                TextureAtlasSprite texture = flowingTexture;
                if (!isLava) {
                    Block var57 = world.getBlockState(var55).getBlock();
                    if (var57 == Blocks.GLASS || var57 instanceof BlockStainedGlass) {
                        texture = atlasSpriteWaterOverlay;
                    }
                }

                float var58 = texture.getInterpolatedU(0);
                float var59 = texture.getInterpolatedU(8);
                float var60 = texture.getInterpolatedV((1 - maxU) * 16 * 0.5);
                float var61 = texture.getInterpolatedV((1 - minV) * 16 * 0.5);
                float var62 = texture.getInterpolatedV(8);
                int var63 = func_204835_a(world, var55);
                int var64 = var63 >> 16;
                float colorDarkness = direction < 2 ? 0.8F : 0.6F;
                float var67 = colorDarkness * redMultiplier;
                float var68 = colorDarkness * greenMultiplier;
                float var69 = colorDarkness * blueMultiplier;
                buffer.pos(x1, y + maxU, z1).color(var67, var68, var69, 1).tex(var58, var60).lightmap(var64, var63).endVertex();
                buffer.pos(x2, y + minV, z2).color(var67, var68, var69, 1).tex(var59, var61).lightmap(var64, var63).endVertex();
                buffer.pos(x2, y, z2).color(var67, var68, var69, 1).tex(var59, var62).lightmap(var64, var63).endVertex();
                buffer.pos(x1, y, z1).color(var67, var68, var69, 1).tex(var58, var62).lightmap(var64, var63).endVertex();
                if (texture != atlasSpriteWaterOverlay) {
                    buffer.pos(x1, y, z1).color(var67, var68, var69, 1).tex(var58, var62).lightmap(var64, var63).endVertex();
                    buffer.pos(x2, y, z2).color(var67, var68, var69, 1).tex(var59, var62).lightmap(var64, var63).endVertex();
                    buffer.pos(x2, y + minV, z2).color(var67, var68, var69, 1).tex(var59, var61).lightmap(var64, var63).endVertex();
                    buffer.pos(x1, y + maxU, z1).color(var67, var68, var69, 1).tex(var58, var60).lightmap(var64, var63).endVertex();
                }
            }
        }

        return rendered;
    }
}
