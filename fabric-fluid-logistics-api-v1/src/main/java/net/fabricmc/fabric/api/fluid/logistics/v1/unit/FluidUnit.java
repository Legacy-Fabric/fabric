/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.fluid.logistics.v1.unit;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import net.fabricmc.fabric.api.fluid.logistics.v1.FluidStack;
import net.fabricmc.fabric.api.movingstuff.v1.Unit;

/**
 * Stores a list of units that apply to {@link FluidStack}s
 *
 * @see net.fabricmc.fabric.api.movingstuff.v1.Unit
 */
public enum FluidUnit implements Unit<FluidUnit> {
	/**
	 * One bucket is equal to one block.
	 * This can also be referred to as a Cubic Meter.
	 */
	BLOCK(1620),
	/**
	 * Cauldrons mechanics imply that three bottles make up
	 * one block of water. A bottle is a third of a bucket.
	 */
	BOTTLE(540),
	/**
	 * Nine ingots make a block in crafting recipes for
	 * minerals such as diamond, gold, iron and emerald.
	 */
	INGOT(180),
	/**
	 * Nine gold nuggets make an ingot in the gold ingot
	 * crafting recipe.
	 */
	NUGGET(20),
	/**
	 * A Droplet is the base unit for fluid transactions.
	 * Internally, droplets are used to store the amount of
	 * fluid.
	 */
	DROPLET(1);

	private static final Map<Integer, FluidUnit> BY_QUANTITY;
	private final int valueAsLowest;

	FluidUnit(int valueAsLowest) {
		this.valueAsLowest = valueAsLowest;
	}

	@Override
	public int getAsLowest() {
		return this.valueAsLowest;
	}

	@Override
	public int getAsHighest() {
		return FluidUnit.BLOCK.valueAsLowest / this.valueAsLowest;
	}

	@Override
	public boolean canConvertTo(int value, FluidUnit to) {
		return (value % to.valueAsLowest) == 0;
	}

	public static FluidUnit byValue(int value) {
		return Optional.ofNullable(BY_QUANTITY.get(value)).orElseThrow(() -> new RuntimeException("Unknown value: " + value));
	}

	static {
		ImmutableMap.Builder<Integer, FluidUnit> builder = ImmutableMap.builder();

		for (FluidUnit unit : values()) {
			builder.put(unit.getAsLowest(), unit);
		}

		BY_QUANTITY = builder.build();
	}
}
