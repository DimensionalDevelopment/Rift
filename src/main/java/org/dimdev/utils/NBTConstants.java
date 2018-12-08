package org.dimdev.utils;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class NBTConstants {
	/** The ID for {@link NBTTagEnd} */
	public static final int END = 0;
	/** The ID for {@link NBTTagByte} */
	public static final int BYTE = 1;
	/** The ID for {@link NBTTagShort} */
	public static final int SHORT = 2;
	/** The ID for {@link NBTTagInt} */
	public static final int INT = 3;
	/** The ID for {@link NBTTagLong} */
	public static final int LONG = 4;
	/** The ID for {@link NBTTagFloat} */
	public static final int FLOAT = 5;
	/** The ID for {@link NBTTagDouble} */
	public static final int DOUBLE = 6;
	/** The ID for {@link NBTTagByteArray} */
	public static final int BYTE_ARRAY = 7;
	/** The ID for {@link NBTTagString} */
	public static final int STRING = 8;
	/** The ID for {@link NBTTagList} */
	public static final int LIST = 9;
	/** The ID for {@link NBTTagCompound} */
	public static final int COMPOUND = 10;
	/** The ID for {@link NBTTagIntArray} */
	public static final int INT_ARRAY = 11;
	/** The ID for {@link NBTTagLongArray} */
	public static final int LONG_ARRAY = 12;
	/** Wildcard ID for any of {@link NBTTagByte}, {@link NBTTagShort}, {@link NBTTagInt}, {@link NBTTagLong}, {@link NBTTagFloat} or {@link NBTTagDouble} */
	public static final int ANY_NUMERIC = 99;
}