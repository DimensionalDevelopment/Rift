package org.dimdev.rift.listener;

import net.minecraft.world.chunk.Chunk;

public interface ChunkEventListener {
    void onChunkLoad(Chunk chunk);
    void onChunkUnload(Chunk chunk);
}
