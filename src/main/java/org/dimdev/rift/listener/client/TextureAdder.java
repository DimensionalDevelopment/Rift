package org.dimdev.rift.listener.client;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public interface TextureAdder {
    Collection<? extends ResourceLocation> getBuiltinTextures();
}
