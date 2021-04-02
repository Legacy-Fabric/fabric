/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.api.util;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

/**
 * NBT type ID constants. Useful for filtering by value type in a few cases.
 *
 * <p>For the current list of types, check with {@link Tag#TYPES}.
 *
 * @see CompoundTag#contains(String, int)
 */
public final class NbtType {
	/**
	 * @see EndTag
	 */
	public static final int END = 0;
	/**
	 * @see ByteTag
	 */
	public static final int BYTE = 1;
	/**
	 * @see ShortTag
	 */
	public static final int SHORT = 2;
	/**
	 * @see IntTag
	 */
	public static final int INT = 3;
	/**
	 * @see LongTag
	 */
	public static final int LONG = 4;
	/**
	 * @see FloatTag
	 */
	public static final int FLOAT = 5;
	/**
	 * @see DoubleTag
	 */
	public static final int DOUBLE = 6;
	/**
	 * @see ByteArrayTag
	 */
	public static final int BYTE_ARRAY = 7;
	/**
	 * @see StringTag
	 */
	public static final int STRING = 8;
	/**
	 * @see ListTag
	 */
	public static final int LIST = 9;
	/**
	 * @see CompoundTag
	 */
	public static final int COMPOUND = 10;
	/**
	 * @see IntArrayTag
	 */
	public static final int INT_ARRAY = 11;
	/**
	 * Any numeric value: byte, short, int, long, float, double.
	 *
	 * @see CompoundTag#contains(String, int)
	 */
	public static final int NUMBER = 99;

	private NbtType() { }
}
