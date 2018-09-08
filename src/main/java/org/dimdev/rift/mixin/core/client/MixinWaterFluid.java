package org.dimdev.rift.mixin.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.BiomeColorHelper;
import org.dimdev.rift.injectedmethods.RiftFluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterFluid.class)
public class MixinWaterFluid implements RiftFluid {
    @Override
    public TextureAtlasSprite getStillTexture() {
        return Minecraft.getInstance().getModelManager().getBlockModelShapes().getModel(Blocks.WATER.getDefaultState()).getParticleTexture();
    }

    @Override
    public TextureAtlasSprite getFlowingTexture() {
        return Minecraft.getInstance().getTextureMap().getSprite(ModelBakery.LOCATION_WATER_FLOW);
    }

    @Override
    public int getColorMultiplier(IWorldReader world, BlockPos pos) {
        return BiomeColorHelper.getWaterColor(world, pos);
    }
}
