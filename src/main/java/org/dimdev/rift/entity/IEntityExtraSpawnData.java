package org.dimdev.rift.entity;

import net.minecraft.network.PacketBuffer;

/**
 * used to de/serialize custom data on entity spawn
 */
public interface IEntityExtraSpawnData {

    void read(PacketBuffer buffer) throws Exception;

    void write(PacketBuffer buffer) throws Exception;
}
