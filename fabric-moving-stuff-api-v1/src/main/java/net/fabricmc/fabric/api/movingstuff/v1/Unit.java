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

package net.fabricmc.fabric.api.movingstuff.v1;

import com.google.common.base.Preconditions;

/**
 * Specifies a unit. All values must be in an enum.
 * Enum constants are very important
 */
public interface Unit<E extends Enum<E> & Unit<E>> {
	/**
	 * @return the value of this unit as the lowest unit.
	 */
	int getAsLowest();

	/**
	 * @return the number of values required to get the highest value.
	 */
	int getAsHighest();

	/**
	 * Converts this unit to a value in another unit.
	 */
	default int to(int value, E to) {
		//noinspection unchecked
		Enum<E> thisEnum = (Enum<E>) this;

		if (this == to) {
			return value;
		} else if (thisEnum.ordinal() < to.ordinal()) {
			return value * (to.getAsHighest() / this.getAsHighest());
		} else {
			int newValue = value * (this.getAsHighest() / to.getAsHighest());
			Preconditions.checkArgument(newValue * this.getAsLowest() == value * to.getAsLowest(), "Can't convert " + value + " to " + to);
			return newValue;
		}
	}

	/**
	 * Checks if this unit can be converted to a value in another unit.
	 */
	default boolean canConvertTo(int value, E to) {
		//noinspection unchecked
		Enum<E> thisEnum = (Enum<E>) this;

		if (thisEnum.ordinal() <= to.ordinal()) {
			return true;
		} else {
			int newValue = value * (this.getAsHighest() / to.getAsHighest());
			return newValue * this.getAsLowest() == value * to.getAsLowest();
		}
	}
}
