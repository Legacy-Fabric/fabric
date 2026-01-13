/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.api.registry.v2.registry.holder;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.util.Identifier;

/**
 * A read-only registry that makes use of numerical ids and thus can require remapping.
 * @param <T> type of entries within the registry
 */
public interface SyncedFabricRegistry<T> extends FabricRegistry<T> {
	/**
	 * Get a registry entry's numerical id.
	 * @param value The registry entry
	 * @return The registry entry's numerical id
	 */
	int fabric$getRawId(T value);

	/**
	 * Get a registry entry's numerical id from its identifier.
	 * @param identifier The registry entry's identifier
	 * @return The registry entry's numerical id
	 */
	default int fabric$getRawId(Identifier identifier) {
		T value = fabric$getValue(identifier);
		return fabric$getRawId(value);
	}

	/**
	 * Get a registry entry from its numerical id.
	 * @param rawId The registry entry's numerical id
	 * @return The registry entry
	 */
	T fabric$getValue(int rawId);

	/**
	 * Get a registry entry's identifier from its numerical id.
	 * @param rawId The registry entry's numerical id
	 * @return The registry entry's identifier
	 */
	default Identifier fabric$getId(int rawId) {
		T value = fabric$getValue(rawId);
		return fabric$getId(value);
	}

	/**
	 * Get the {@link Event} for the {@link RegistryRemapCallback} for this registry.
	 *
	 * @return the event
	 */
	Event<RegistryRemapCallback<T>> fabric$getRegistryRemapCallback();
}
