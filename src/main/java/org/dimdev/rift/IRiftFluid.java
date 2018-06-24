package org.dimdev.rift;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface IRiftFluid {
    TextureAtlasSprite getStillTexture();
    TextureAtlasSprite getFlowingTexture();
    int getColorMultiplier();
}
