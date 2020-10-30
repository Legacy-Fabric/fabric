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

import java.util.List;
import java.util.Map;

import net.minecraft.util.math.Direction;

/**
 * Specifies an object that knows something about thing
 *
 * @param <T> thing
 * @param <U> unit
 */
public interface Aware<T, U extends Unit<? extends Enum<?>>> {
	/**
	 * Gets the maximum number of instances of {@link T} that can be stored.
	 */
	int getMaxCapacity();

	/**
	 * Gets the total number of instances stored from a certain direction.
	 *
	 * @param fromSide the direction
	 * @return the total amount of instances stored from a certain direction
	 */
	default int getCurrentFill(Direction fromSide) {
		int amount = 0;

		for (Instance<T> inst : this.getInstances(fromSide)) {
			amount += inst.getAmount();
		}

		return amount;
	}

	/**
	 * Returns a map of units.
	 */
	Map<U, Integer> getCurrentFillMap(Direction fromSide);

	/**
	 * Gets the instances from a certain direction
	 *
	 * @param fromSide the direction
	 * @return the total amount of instances stored from a certain direction
	 */
	List<Instance<T>> getInstances(Direction fromSide);

	/**
	 * Gets the total number of instances of a certain thing
	 *
	 * @param fromSide the direction
	 * @param thing the thing
	 * @return the total number of instances of a certain thing
	 */
	default int getCurrentSingleFill(Direction fromSide, T thing) {
		int amount = 0;

		for (Instance<T> inst : this.getInstances(fromSide)) {
			if (inst.get() == thing) {
				amount += inst.getAmount();
			}
		}

		return amount;
	}

	/**
	 * Returns a map of units.
	 */
	Map<U, Integer> getCurrentSingleFillMap(Direction fromSide, T thing);
}
