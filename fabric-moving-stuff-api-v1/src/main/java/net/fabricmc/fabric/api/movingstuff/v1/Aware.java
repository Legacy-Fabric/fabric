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

import net.minecraft.util.math.Direction;

/**
 * Specifies an object that knows something about thing
 *
 * @param <T> thing
 */
public interface Aware<T> {
	int getMaxCapacity();

	default int getCurrentFill(Direction fromSide) {
		int amount = 0;

		for (Instance<T> inst : this.getInstances(fromSide)) {
			amount += inst.getAmount();
		}

		return amount;
	}

	List<Instance<T>> getInstances(Direction fromSide);

	default int getCurrentSingleFill(Direction fromSide, T thing) {
		int amount = 0;

		for (Instance<T> inst : this.getInstances(fromSide)) {
			if (inst.get() == thing) {
				amount += inst.getAmount();
			}
		}

		return amount;
	}
}
