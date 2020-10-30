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

import net.minecraft.util.math.Direction;

/**
 * Base class for objects that can have a certain thing extracted out from them.
 *
 * @param <T> thing
 * @param <U> unit
 */
public interface Extractable<T, U extends Unit<? extends Enum<?>>> extends Aware<T, U> {
	/**
	 * Extracts a certain amount of thing into from extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param thing the type of thing to extract.
	 * @param amount how much thing to extract.
	 */
	void extract(Direction fromSide, T thing, int amount);

	/**
	 * Extracts a certain amount of thing into from extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param thing the type of thing to extract.
	 * @param amount how much thing to extract.
	 * @param unit the unit
	 */
	default void extract(Direction fromSide, T thing, int amount, U unit) {
		this.extract(fromSide, thing, amount * unit.getAsLowest());
	}

	/**
	 * Checks whether a certain amount of thing can be extracted from this extractable
	 *
	 * @param fromSide the side from which to extract.
	 * @param thing the type of thing to extract.
	 * @param amount how much thing to extract.
	 * @return whether the extraction is possible
	 */
	boolean canExtract(Direction fromSide, T thing, int amount);

	/**
	 * Checks whether a certain amount of thing can be extracted from this extractable
	 *
	 * @param fromSide the side from which to extract.
	 * @param thing the type of thing to extract.
	 * @param amount how much thing to extract.
	 * @param unit the unit
	 * @return whether the extraction is possible
	 */
	default boolean canExtract(Direction fromSide, T thing, int amount, U unit) {
		return this.canExtract(fromSide, thing, amount * unit.getAsLowest());
	}

	/**
	 * Attempts to extract thing from this extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param thing the type of thing to extract.
	 * @param amount how much thing to extract.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return whether the extraction was/would be successful.
	 */
	default boolean tryExtract(Direction fromSide, T thing, int amount, boolean simulate) {
		if (this.canExtract(fromSide, thing, amount)) {
			if (!simulate) {
				this.extract(fromSide, thing, amount);
			}
			return true;
		}
		return false;
	}

	/**
	 * Attempts to extract thing from this extractable.
	 *
	 * @param fromSide the side from which to extract.
	 * @param thing the type of thing to extract.
	 * @param amount how much thing to extract.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @param unit the unit
	 * @return whether the extraction was/would be successful.
	 */
	default boolean tryExtract(Direction fromSide, T thing, int amount, boolean simulate, U unit) {
		return this.tryExtract(fromSide, thing, amount * unit.getAsLowest(), simulate);
	}

	/**
	 * Attempt to extract thing, only draining partially if the container can't hold all the thing.
	 *
	 * @param fromSide the side from which to extract.
	 * @param thing the type of thing to extract.
	 * @param maxAmount how much thing to extract at maximum.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return an integer amount of how much thing was/would be moved.
	 */
	default int tryPartialExtract(Direction fromSide, T thing, int maxAmount, boolean simulate) {
		int remaining = this.getCurrentSingleFill(fromSide, thing);
		int amount = Math.min(maxAmount, remaining);

		if (this.canExtract(fromSide, thing, amount)) {
			if (!simulate) {
				this.extract(fromSide, thing, amount);
			}

			return amount;
		}

		return 0;
	}
}
