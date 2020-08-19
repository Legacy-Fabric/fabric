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

package net.fabricmc.fabric.impl.biomes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import net.fabricmc.fabric.impl.biomes.WeightedPicker.WeightedEntry;

/**
 * Picks entries with arbitrary double weights using a binary search.
 * Adapted and Generalised from fabric API's WeightedBiomePicker.
 */
public final class WeightedPicker<T extends WeightedEntry> {
	private double currentTotal;
	private final List<T> entries = new ArrayList<>();;

	public WeightedPicker() {
		currentTotal = 0;
	}

	void add(final T entry) {
		this.currentTotal += entry.getWeight();
		entry.updateUpperWeightBound(this.currentTotal);
		this.entries.add(entry);
	}

	double getCurrentWeightTotal() {
		return this.currentTotal;
	}

	public T pickRandom(LayerRandom random) {
		double target = random.nextInt(Integer.MAX_VALUE) * getCurrentWeightTotal() / Integer.MAX_VALUE;
		return this.search(target);
	}

	/**
	 * Searches with the specified target value.
	 *
	 * @param target The target value, must satisfy the constraint 0 <= target <= currentTotal
	 * @return The result of the search
	 */
	T search(final double target) {
		// Sanity checks, fail fast if stuff is going wrong.
		Preconditions.checkArgument(target <= this.currentTotal, "The provided target value for selection must be less than or equal to the weight total");
		Preconditions.checkArgument(target >= 0, "The provided target value for selection cannot be negative");

		int low = 0;
		int high = this.entries.size() - 1;

		while (low < high) {
			int mid = (high + low) >>> 1;

			if (target < this.entries.get(mid).getUpperWeightBound()) {
				high = mid;
			} else {
				low = mid + 1;
			}
		}

		return this.entries.get(low);
	}

	public interface WeightedEntry {
		double getUpperWeightBound();
		double getWeight();
		void updateUpperWeightBound(double currentTotal);
	}
}