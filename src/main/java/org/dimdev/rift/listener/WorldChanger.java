package org.dimdev.rift.listener;

import net.minecraft.world.biome.Biome;

@ListenerInterface
public interface WorldChanger {
    void modifyBiome(int biomeId, String biomeName, Biome biome);
}
