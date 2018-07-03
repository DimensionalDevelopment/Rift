package org.dimdev.rift.mixin.core.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
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
import net.minecraft.world.biome.BiomeColorHelper;
import org.dimdev.rift.IRiftFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockFluidRenderer.class)
public abstract class MixinBlockFluidRenderer {
    @Shadow protected abstract float func_204504_a(IWorldReaderBase p_204504_1_, BlockPos p_204504_2_, Fluid p_204504_3_);
    @Shadow protected abstract int func_204835_a(IWorldReader p_204835_1_, BlockPos p_204835_2_);
    @Shadow private static boolean func_209557_a(IBlockReader p_209557_0_, BlockPos p_209557_1_, EnumFacing p_209557_2_, IFluidState p_209557_3_) { return false; }
    @Shadow private static boolean func_209556_a(IBlockReader p_209556_0_, BlockPos p_209556_1_, EnumFacing p_209556_2_, float p_209556_3_) { return false; }

    @Shadow private TextureAtlasSprite atlasSpriteWaterOverlay;
    @Shadow @Final private TextureAtlasSprite[] atlasSpritesLava;
    @Shadow @Final private TextureAtlasSprite[] atlasSpritesWater;

    @Overwrite // TODO: don't overwrite
    public boolean func_205346_a(IWorldReader world, BlockPos pos, BufferBuilder buffer, IFluidState state) {
        boolean isLava = state.isTagged(FluidTags.LAVA);
        TextureAtlasSprite[] sprites;
        int colorMultiplier;

        if (state.getFluid() instanceof IRiftFluid) {
            IRiftFluid fluid = (IRiftFluid) state.getFluid();
            sprites = new TextureAtlasSprite[] {fluid.getStillTexture(), fluid.getFlowingTexture()};
            colorMultiplier = fluid.getColorMultiplier();
        } else {
            sprites = isLava ? atlasSpritesLava : atlasSpritesWater;
            colorMultiplier = isLava ? 16777215 : BiomeColorHelper.getWaterColorAtPos(world, pos);
        }

        float redMultiplier = (colorMultiplier >> 16 & 255) / 255F;
        float greenMultiplier = (colorMultiplier >> 8 & 255) / 255F;
        float blueMultiplier = (colorMultiplier & 255) / 255F;

        boolean var6 = !func_209557_a(world, pos, EnumFacing.UP, state);
        boolean var7 = !func_209557_a(world, pos, EnumFacing.DOWN, state) && !func_209556_a(world, pos, EnumFacing.DOWN, 0.8888889F);
        boolean var8 = !func_209557_a(world, pos, EnumFacing.NORTH, state);
        boolean var9 = !func_209557_a(world, pos, EnumFacing.SOUTH, state);
        boolean var10 = !func_209557_a(world, pos, EnumFacing.WEST, state);
        boolean var11 = !func_209557_a(world, pos, EnumFacing.EAST, state);
        if (!var6 && !var7 && !var11 && !var10 && !var8 && !var9) {
            return false;
        } else {
            boolean var12 = false;
            float var17 = func_204504_a(world, pos, state.getFluid());
            float var18 = func_204504_a(world, pos.south(), state.getFluid());
            float var19 = func_204504_a(world, pos.east().south(), state.getFluid());
            float var20 = func_204504_a(world, pos.east(), state.getFluid());
            double var21 = (double)pos.getX();
            double var22 = (double)pos.getY();
            double var23 = (double)pos.getZ();
            float var27;
            float var29;
            float var31;
            float var33;
            float var34;
            if (var6 && !func_209556_a(world, pos, EnumFacing.UP, Math.min(Math.min(var17, var18), Math.min(var19, var20)))) {
                var12 = true;
                var17 -= 0.001F;
                var18 -= 0.001F;
                var19 -= 0.001F;
                var20 -= 0.001F;
                Vec3d var25 = state.func_206887_a(world, pos);
                float var28;
                float var30;
                float var32;
                TextureAtlasSprite var26;
                float var37;
                float var38;
                if (var25.x == 0.0D && var25.z == 0.0D) {
                    var26 = sprites[0];
                    var27 = var26.getInterpolatedU(0.0D);
                    var28 = var26.getInterpolatedV(0.0D);
                    var29 = var27;
                    var30 = var26.getInterpolatedV(16.0D);
                    var31 = var26.getInterpolatedU(16.0D);
                    var32 = var30;
                    var33 = var31;
                    var34 = var28;
                } else {
                    var26 = sprites[1];
                    float var35 = (float)MathHelper.atan2(var25.z, var25.x) - 1.5707964F;
                    float var36 = MathHelper.sin(var35) * 0.25F;
                    var37 = MathHelper.cos(var35) * 0.25F;
                    var27 = var26.getInterpolatedU((double)(8.0F + (-var37 - var36) * 16.0F));
                    var28 = var26.getInterpolatedV((double)(8.0F + (-var37 + var36) * 16.0F));
                    var29 = var26.getInterpolatedU((double)(8.0F + (-var37 + var36) * 16.0F));
                    var30 = var26.getInterpolatedV((double)(8.0F + (var37 + var36) * 16.0F));
                    var31 = var26.getInterpolatedU((double)(8.0F + (var37 + var36) * 16.0F));
                    var32 = var26.getInterpolatedV((double)(8.0F + (var37 - var36) * 16.0F));
                    var33 = var26.getInterpolatedU((double)(8.0F + (var37 - var36) * 16.0F));
                    var34 = var26.getInterpolatedV((double)(8.0F + (-var37 - var36) * 16.0F));
                }

                int var39 = func_204835_a(world, pos);
                int var40 = var39 >> 16;
                var37 = 1.0F * redMultiplier;
                var38 = 1.0F * greenMultiplier;
                float var42 = 1.0F * blueMultiplier;
                buffer.pos(var21 + 0.0D, var22 + (double)var17, var23 + 0.0D).color(var37, var38, var42, 1.0F).tex((double)var27, (double)var28).lightmap(var40, var39).endVertex();
                buffer.pos(var21 + 0.0D, var22 + (double)var18, var23 + 1.0D).color(var37, var38, var42, 1.0F).tex((double)var29, (double)var30).lightmap(var40, var39).endVertex();
                buffer.pos(var21 + 1.0D, var22 + (double)var19, var23 + 1.0D).color(var37, var38, var42, 1.0F).tex((double)var31, (double)var32).lightmap(var40, var39).endVertex();
                buffer.pos(var21 + 1.0D, var22 + (double)var20, var23 + 0.0D).color(var37, var38, var42, 1.0F).tex((double)var33, (double)var34).lightmap(var40, var39).endVertex();
                if (state.func_205586_a(world, pos.up())) {
                    buffer.pos(var21 + 0.0D, var22 + (double)var17, var23 + 0.0D).color(var37, var38, var42, 1.0F).tex((double)var27, (double)var28).lightmap(var40, var39).endVertex();
                    buffer.pos(var21 + 1.0D, var22 + (double)var20, var23 + 0.0D).color(var37, var38, var42, 1.0F).tex((double)var33, (double)var34).lightmap(var40, var39).endVertex();
                    buffer.pos(var21 + 1.0D, var22 + (double)var19, var23 + 1.0D).color(var37, var38, var42, 1.0F).tex((double)var31, (double)var32).lightmap(var40, var39).endVertex();
                    buffer.pos(var21 + 0.0D, var22 + (double)var18, var23 + 1.0D).color(var37, var38, var42, 1.0F).tex((double)var29, (double)var30).lightmap(var40, var39).endVertex();
                }
            }

            if (var7) {
                var27 = sprites[0].getMinU();
                var29 = sprites[0].getMaxU();
                var31 = sprites[0].getMinV();
                var33 = sprites[0].getMaxV();
                int var43 = func_204835_a(world, pos.down());
                int var44 = var43 >> 16;
                var34 = 0.5F * redMultiplier;
                float var46 = 0.5F * greenMultiplier;
                float var47 = 0.5F * blueMultiplier;
                buffer.pos(var21, var22, var23 + 1.0D).color(var34, var46, var47, 1.0F).tex((double)var27, (double)var33).lightmap(var44, var43).endVertex();
                buffer.pos(var21, var22, var23).color(var34, var46, var47, 1.0F).tex((double)var27, (double)var31).lightmap(var44, var43).endVertex();
                buffer.pos(var21 + 1.0D, var22, var23).color(var34, var46, var47, 1.0F).tex((double)var29, (double)var31).lightmap(var44, var43).endVertex();
                buffer.pos(var21 + 1.0D, var22, var23 + 1.0D).color(var34, var46, var47, 1.0F).tex((double)var29, (double)var33).lightmap(var44, var43).endVertex();
                var12 = true;
            }

            for(int var48 = 0; var48 < 4; ++var48) {
                double var49;
                double var51;
                double var50;
                double var52;
                EnumFacing var53;
                boolean var54;
                if (var48 == 0) {
                    var29 = var17;
                    var31 = var20;
                    var49 = var21;
                    var50 = var21 + 1.0D;
                    var51 = var23 + 0.0010000000474974513D;
                    var52 = var23 + 0.0010000000474974513D;
                    var53 = EnumFacing.NORTH;
                    var54 = var8;
                } else if (var48 == 1) {
                    var29 = var19;
                    var31 = var18;
                    var49 = var21 + 1.0D;
                    var50 = var21;
                    var51 = var23 + 1.0D - 0.0010000000474974513D;
                    var52 = var23 + 1.0D - 0.0010000000474974513D;
                    var53 = EnumFacing.SOUTH;
                    var54 = var9;
                } else if (var48 == 2) {
                    var29 = var18;
                    var31 = var17;
                    var49 = var21 + 0.0010000000474974513D;
                    var50 = var21 + 0.0010000000474974513D;
                    var51 = var23 + 1.0D;
                    var52 = var23;
                    var53 = EnumFacing.WEST;
                    var54 = var10;
                } else {
                    var29 = var20;
                    var31 = var19;
                    var49 = var21 + 1.0D - 0.0010000000474974513D;
                    var50 = var21 + 1.0D - 0.0010000000474974513D;
                    var51 = var23;
                    var52 = var23 + 1.0D;
                    var53 = EnumFacing.EAST;
                    var54 = var11;
                }

                if (var54 && !func_209556_a(world, pos, var53, Math.max(var29, var31))) {
                    var12 = true;
                    BlockPos var55 = pos.offset(var53);
                    TextureAtlasSprite var56 = sprites[1];
                    if (!isLava) {
                        Block var57 = world.getBlockState(var55).getBlock();
                        if (var57 == Blocks.GLASS || var57 instanceof BlockStainedGlass) {
                            var56 = atlasSpriteWaterOverlay;
                        }
                    }

                    float var58 = var56.getInterpolatedU(0.0D);
                    float var59 = var56.getInterpolatedU(8.0D);
                    float var60 = var56.getInterpolatedV((double)((1.0F - var29) * 16.0F * 0.5F));
                    float var61 = var56.getInterpolatedV((double)((1.0F - var31) * 16.0F * 0.5F));
                    float var62 = var56.getInterpolatedV(8.0D);
                    int var63 = func_204835_a(world, var55);
                    int var64 = var63 >> 16;
                    float var66 = var48 < 2 ? 0.8F : 0.6F;
                    float var67 = 1.0F * var66 * redMultiplier;
                    float var68 = 1.0F * var66 * greenMultiplier;
                    float var69 = 1.0F * var66 * blueMultiplier;
                    buffer.pos(var49, var22 + (double)var29, var51).color(var67, var68, var69, 1.0F).tex((double)var58, (double)var60).lightmap(var64, var63).endVertex();
                    buffer.pos(var50, var22 + (double)var31, var52).color(var67, var68, var69, 1.0F).tex((double)var59, (double)var61).lightmap(var64, var63).endVertex();
                    buffer.pos(var50, var22 + 0.0D, var52).color(var67, var68, var69, 1.0F).tex((double)var59, (double)var62).lightmap(var64, var63).endVertex();
                    buffer.pos(var49, var22 + 0.0D, var51).color(var67, var68, var69, 1.0F).tex((double)var58, (double)var62).lightmap(var64, var63).endVertex();
                    if (var56 != atlasSpriteWaterOverlay) {
                        buffer.pos(var49, var22 + 0.0D, var51).color(var67, var68, var69, 1.0F).tex((double)var58, (double)var62).lightmap(var64, var63).endVertex();
                        buffer.pos(var50, var22 + 0.0D, var52).color(var67, var68, var69, 1.0F).tex((double)var59, (double)var62).lightmap(var64, var63).endVertex();
                        buffer.pos(var50, var22 + (double)var31, var52).color(var67, var68, var69, 1.0F).tex((double)var59, (double)var61).lightmap(var64, var63).endVertex();
                        buffer.pos(var49, var22 + (double)var29, var51).color(var67, var68, var69, 1.0F).tex((double)var58, (double)var60).lightmap(var64, var63).endVertex();
                    }
                }
            }

            return var12;
        }
    }
}
