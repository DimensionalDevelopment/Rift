package org.dimdev.rift.util;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * Base interface for (de)serializing objects of type {@code T}
 * to and from {@link NBTTagCompound} tag compounds
 */
public interface NBTSerializer<T> {
    /**
     * Writes an object {@code T} to the given compound
     * @param compound The tag compound to be written to
     * @return The written tag compound
     */
    @Nonnull
    NBTTagCompound serialize(@Nonnull NBTTagCompound compound, @Nonnull T instance);

    /**
     * Reads an object {@code T} from the given compound
     * @param compound The tag compound to be read from
     * @return An instance of {@code T}
     */
    @Nonnull
    T deserialize(@Nonnull NBTTagCompound compound);

    /**
     * Writes an object {@code T} to a new tag compound
     * @return The written tag compound
     */
    @Nonnull
    default NBTTagCompound serialize(@Nonnull T instance) {
        return this.serialize(new NBTTagCompound(), instance);
    }
}
