/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.util.Identifier;

/**
 * A basic read-only registry.
 * @param <T> type of entries within the registry
 */
public interface FabricRegistry<T> extends Iterable<T> {
	/**
	 * @return The registry unique id.
	 */
	Identifier fabric$getId();

	/**
	 * Get the {@link Event} for the {@link RegistryEntryAddedCallback} for this registry.
	 *
	 * @return the event
	 */
	Event<RegistryEntryAddedCallback<T>> fabric$getEntryAddedCallback();

	/**
	 * Get the {@link Event} for the {@link RegistryBeforeAddCallback} for this registry.
	 *
	 * @return the event
	 */
	Event<RegistryBeforeAddCallback<T>> fabric$getBeforeAddedCallback();

	/**
	 * Convert a {@link Identifier} into the registry's internal key object type.
	 * @param identifier the identifier to convert
	 * @return the converted identifier as registry's internal key object type
	 * @param <K> registry's internal key object type
	 */
	<K> K fabric$toKeyType(Identifier identifier);

	/**
	 * Get a registry entry from its identifier.
	 * @param id the registry entry identifier
	 * @return the registry entry
	 */
	T fabric$getValue(Identifier id);

	/**
	 * Get a registry entry's identifier.
	 * @param value The registry entry
	 * @return The registry entry's identifier
	 */
	Identifier fabric$getId(T value);

	/**
	 * @return A stream of this registry entries
	 */
	default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
