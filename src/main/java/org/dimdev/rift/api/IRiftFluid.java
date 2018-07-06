package org.dimdev.rift.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public interface IRiftFluid {
    TextureAtlasSprite getStillTexture();
    TextureAtlasSprite getFlowingTexture();
    int getColorMultiplier(IWorldReader world, BlockPos pos);
}
