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

/**
 * NBT type ID constants. Useful for filtering by value type in a few cases.
 *
 * <p>For the current list of types, check with {@link net.minecraft.nbt.NbtElement#TYPES}.
 *
 * @see net.minecraft.nbt.NbtCompound#contains(String, int)
 */
public final class NbtType {
	/**
	 * @see net.minecraft.nbt.NbtEnd
	 */
	public static final int END = 0;
	/**
	 * @see net.minecraft.nbt.NbtByte
	 */
	public static final int BYTE = 1;
	/**
	 * @see net.minecraft.nbt.NbtShort
	 */
	public static final int SHORT = 2;
	/**
	 * @see net.minecraft.nbt.NbtInt
	 */
	public static final int INT = 3;
	/**
	 * @see net.minecraft.nbt.NbtLong
	 */
	public static final int LONG = 4;
	/**
	 * @see net.minecraft.nbt.NbtFloat
	 */
	public static final int FLOAT = 5;
	/**
	 * @see net.minecraft.nbt.NbtDouble
	 */
	public static final int DOUBLE = 6;
	/**
	 * @see net.minecraft.nbt.NbtByteArray
	 */
	public static final int BYTE_ARRAY = 7;
	/**
	 * @see net.minecraft.nbt.NbtString
	 */
	public static final int STRING = 8;
	/**
	 * @see net.minecraft.nbt.NbtList
	 */
	public static final int LIST = 9;
	/**
	 * @see net.minecraft.nbt.NbtCompound
	 */
	public static final int COMPOUND = 10;
	/**
	 * @see net.minecraft.nbt.NbtIntArray
	 */
	public static final int INT_ARRAY = 11;
	/**
	 * Any numeric value: byte, short, int, long, float, double.
	 *
	 * @see net.minecraft.nbt.NbtCompound#contains(String, int)
	 */
	public static final int NUMBER = 99;

	private NbtType() {
	}
}
