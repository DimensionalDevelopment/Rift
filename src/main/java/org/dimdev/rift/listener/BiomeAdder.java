package org.dimdev.rift.listener;

import net.minecraft.world.biome.Biome;

import java.util.Collection;

@ListenerInterface
public interface BiomeAdder {
    void registerBiomes();
    Collection<Biome> getOverworldBiomes();
}
