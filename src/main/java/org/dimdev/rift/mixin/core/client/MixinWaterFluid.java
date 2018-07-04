package org.dimdev.rift.mixin.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.BiomeColorHelper;
import org.dimdev.rift.IRiftFluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterFluid.class)
public class MixinWaterFluid implements IRiftFluid {
    @Override
    public TextureAtlasSprite getStillTexture() {
        return Minecraft.getMinecraft().getModelManager().getBlockModelShapes().getModelForState(Blocks.WATER.getDefaultState()).getParticleTexture();
    }

    @Override
    public TextureAtlasSprite getFlowingTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().func_195424_a(ModelBakery.field_207768_f);
    }

    @Override
    public int getColorMultiplier(IWorldReader world, BlockPos pos) {
        return BiomeColorHelper.getWaterColorAtPos(world, pos);
    }
}
