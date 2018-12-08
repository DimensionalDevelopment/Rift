package org.dimdev.rift.injectedmethods;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public interface RiftFluid {
    TextureAtlasSprite getStillTexture();
    TextureAtlasSprite getFlowingTexture();
    int getColorMultiplier(IWorldReader world, BlockPos pos);

    /**
     * Optifine changes how fluid colouring is calculated, returning true reverts this back to what {@link RiftFluid#getColorMultiplier(IWorldReader, BlockPos)} returns.
     * <br/>
     * When Optifine is not present this method is unused and {@link RiftFluid#getColorMultiplier(IWorldReader, BlockPos)} is always used.
     * 
     * @return Whether to rely on the colour Optifine works out (if present) or use {@link RiftFluid#getColorMultiplier(IWorldReader, BlockPos)}
     */
    default boolean ignoreOptifine() {
    	return true;
    }
}
