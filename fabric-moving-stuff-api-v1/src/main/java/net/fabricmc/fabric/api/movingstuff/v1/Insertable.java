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
 * Base class for objects that can have a certain thing inserted into them.
 *
 * @param <T> thing
 * @param <U> unit
 */
public interface Insertable<T, U extends Unit<? extends Enum<?>>> extends Aware<T, U> {
	/**
	 * Inserts a certain amount of thing into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 */
	void insert(Direction fromSide, T thing, int amount);

	/**
	 * Inserts a certain amount of thing into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 * @param unit the unit
	 */
	default void insert(Direction fromSide, T thing, int amount, U unit) {
		this.insert(fromSide, thing, amount * unit.getAsLowest());
	}

	/**
	 * Checks whether a certain amount of thing can be inserted into this insertable
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 * @return whether the insertion is possible
	 */
	boolean canInsert(Direction fromSide, T thing, int amount);

	/**
	 * Checks whether a certain amount of thing can be inserted into this insertable
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 * @param unit the unit
	 * @return whether the insertion is possible
	 */
	default boolean canInsert(Direction fromSide, T thing, int amount, U unit) {
		return this.canInsert(fromSide, thing, amount * unit.getAsLowest());
	}

	/**
	 * Attempt to insert thing into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return whether the insertion was/would be successful.
	 */
	default boolean tryInsert(Direction fromSide, T thing, int amount, boolean simulate) {
		if (this.canInsert(fromSide, thing, amount)) {
			if (!simulate) {
				this.insert(fromSide, thing, amount);
			}

			return true;
		}

		return false;
	}

	/**
	 * Attempt to insert thing into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @param unit the unit
	 * @return whether the insertion was/would be successful.
	 */
	default boolean tryInsert(Direction fromSide, T thing, int amount, boolean simulate, U unit) {
		return this.tryInsert(fromSide, thing, amount * unit.getAsLowest(), simulate);
	}

	/**
	 * Attempt to insert thing, only filling partially if the container can't hold all the thing.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param maxAmount how much thing to insert at maximum.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return an integer amount of how much thing was/would be moved.
	 */
	default int tryPartialInsert(Direction fromSide, T thing, int maxAmount, boolean simulate) {
		int remainingCapacity = this.getMaxCapacity() - this.getCurrentFill(fromSide);
		int amount = Math.min(maxAmount, remainingCapacity);

		if (this.canInsert(fromSide, thing, amount)) {
			if (!simulate) {
				this.insert(fromSide, thing, amount);
			}

			return amount;
		}

		return 0;
	}

	/**
	 * Attempt to insert thing, only filling partially if the container can't hold all the thing.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param maxAmount how much thing to insert at maximum.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @param unit the unit
	 * @return an integer amount of how much thing was/would be moved.
	 */
	default int tryPartialInsert(Direction fromSide, T thing, int maxAmount, boolean simulate, U unit) {
		return this.tryPartialInsert(fromSide, thing, maxAmount * unit.getAsLowest(), simulate);
	}
}
