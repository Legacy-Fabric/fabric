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

import java.util.Optional;

import net.minecraft.nbt.CompoundTag;

/**
 * Stores an instance of thing, the amount of thing stored,
 * and the tag associated with the thing
 *
 * @param <T> thing
 */
public interface Instance<T> {
	/**
	 * @return thing
	 */
	Optional<T> get();

	/**
	 * @return the amount of thing stored
	 */
	int getAmount();

	/**
	 * Sets the amount of thing.
	 */
	void setAmount(int amount);

	/**
	 * @return the tag associated with the thing.
	 */
	CompoundTag getTag();

	/**
	 * Sets the tag associated with the thing.
	 */
	void setTag(CompoundTag tag);

	/**
	 * Serializes this instance into a tag.
	 *
	 * @param tag the tag that should store the values
	 * @return the tag passed as a parameter
	 */
	CompoundTag toTag(CompoundTag tag);

	/**
	 * Deserializes this instance from a tag.
	 *
	 * @param tag the tag that should be deserialized from
	 */
	void fromTag(CompoundTag tag);
}
