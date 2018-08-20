package org.dimdev.rift.listener.client;

import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.ListenerInterface;

import java.util.Collection;

@ListenerInterface
public interface TextureAdder {
    Collection<? extends ResourceLocation> getBuiltinTextures();
}
