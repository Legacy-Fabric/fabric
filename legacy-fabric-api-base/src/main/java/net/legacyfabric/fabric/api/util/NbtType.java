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

import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtEnd;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

/**
 * NBT type ID constants. Useful for filtering by value type in a few cases.
 *
 * <p>For the current list of types, check with {@link AbstractNbtNumber#TYPES}.
 *
 * @see NbtCompound#contains(String, int)
 */
public final class NbtType {
	/**
	 * @see NbtEnd
	 */
	public static final int END = 0;
	/**
	 * @see NbtByte
	 */
	public static final int BYTE = 1;
	/**
	 * @see NbtShort
	 */
	public static final int SHORT = 2;
	/**
	 * @see NbtInt
	 */
	public static final int INT = 3;
	/**
	 * @see NbtLong
	 */
	public static final int LONG = 4;
	/**
	 * @see NbtFloat
	 */
	public static final int FLOAT = 5;
	/**
	 * @see NbtDouble
	 */
	public static final int DOUBLE = 6;
	/**
	 * @see NbtByteArray
	 */
	public static final int BYTE_ARRAY = 7;
	/**
	 * @see NbtString
	 */
	public static final int STRING = 8;
	/**
	 * @see NbtList
	 */
	public static final int LIST = 9;
	/**
	 * @see NbtCompound
	 */
	public static final int COMPOUND = 10;
	/**
	 * @see NbtIntArray
	 */
	public static final int INT_ARRAY = 11;
	/**
	 * Any numeric value: byte, short, int, long, float, double.
	 *
	 * @see NbtCompound#contains(String, int)
	 */
	public static final int NUMBER = 99;

	private NbtType() {
	}
}
