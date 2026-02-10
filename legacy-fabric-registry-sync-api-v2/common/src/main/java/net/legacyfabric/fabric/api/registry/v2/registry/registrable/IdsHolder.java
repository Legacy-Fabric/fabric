/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.api.registry.v2.registry.registrable;

/**
 * Holds the mappings between registry entries and their numerical ids.<br>
 * Vanilla implementations: {@link net.minecraft.util.CrudeIncrementalIntIdentityHashMap}
 * @param <T> type of entries
 */
public interface IdsHolder<T> extends Iterable<T> {
	/**
	 * Creates a new empty instance of this IdsHolder type, with the same initial settings.
	 * @return a new IdsHolder instance
	 */
	IdsHolder<T> fabric$new();

	/**
	 * @return The next free numerical id
	 */
	int fabric$nextId();

	/**
	 * Set an entry-id mapping.
	 * @param value Entry
	 * @param id Numerical id
	 */
	void fabric$setValue(T value, int id);

	/**
	 * @return The number of mappings
	 */
	int fabric$size();

	/**
	 * Get the numerical id associated to an entry, returns -1 if none can be found.
	 * @param value The entry
	 * @return The entry's numerical id,
	 * -1 is returned if the entry doesn't have an associated id
	 */
	int fabric$getId(T value);

	/**
	 * Check whether the provided entry has an associated numerical id.
	 * @param value The entry
	 * @return whether the entry has an associated numerical id
	 */
	default boolean fabric$contains(T value) {
		return fabric$getId(value) != -1;
	}

	/**
	 * Get the entry associated to a numerical id, returns null if none can be found.
	 * @param rawId The entry's numerical id
	 * @return The entry
	 */
	T fabric$getValue(int rawId);
}
