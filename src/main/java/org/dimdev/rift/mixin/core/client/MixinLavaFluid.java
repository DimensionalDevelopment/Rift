package org.dimdev.rift.mixin.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import org.dimdev.rift.injectedmethods.RiftFluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LavaFluid.class)
public class MixinLavaFluid implements RiftFluid {
    @Override
    public TextureAtlasSprite getStillTexture() {
        return Minecraft.getMinecraft().getModelManager().getBlockModelShapes().getModelForState(Blocks.LAVA.getDefaultState()).getParticleTexture();
    }

    @Override
    public TextureAtlasSprite getFlowingTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().func_195424_a(ModelBakery.field_207766_d);
    }

    @Override
    public int getColorMultiplier(IWorldReader world, BlockPos pos) {
        return 0xFFFFFF;
    }
}
