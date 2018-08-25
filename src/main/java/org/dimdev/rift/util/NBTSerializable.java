package org.dimdev.rift.util;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * Base interface for (de)serializable objects to serialize
 * themselves to and from {@link NBTTagCompound} tag compounds
 */
public interface NBTSerializable {
    /**
     * Writes this object's data to the given compound
     * @param compound The tag compound to be written to
     * @return The written tag compound
     */
    @Nonnull
    NBTTagCompound serialize(@Nonnull NBTTagCompound compound);

    /**
     * Reads this object's data from the given compound
     * @param compound The tag compound to be read from
     * @return The given tag compound
     */
    @Nonnull
    NBTTagCompound deserialize(@Nonnull NBTTagCompound compound);

    /**
     * Writes this object's data to a new tag compound
     * @return The written tag compound
     */
    @Nonnull
    default NBTTagCompound writeToNBT() {
        return this.serialize(new NBTTagCompound());
    }
}
