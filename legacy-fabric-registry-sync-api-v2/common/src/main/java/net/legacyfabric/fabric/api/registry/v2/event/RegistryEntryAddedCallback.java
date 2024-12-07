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

package net.legacyfabric.fabric.api.registry.v2.event;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryEventHelper;

/**
 * An event for when an entry is added to a registry.
 *
 * @param <T> the type of the entry within the registry
 */
@FunctionalInterface
public interface RegistryEntryAddedCallback<T> {
	/**
	 * Called when a new entry is added to the registry.
	 *
	 * @param rawId the raw id of the entry
	 * @param id the identifier of the entry
	 * @param object the object that was added
	 */
	void onEntryAdded(int rawId, Identifier id, T object);

	/**
	 * Get the {@link Event} for the {@link RegistryEntryAddedCallback} for the given registry.
	 *
	 * @param registryId the id of the registry to get the event for
	 * @return the event
	 */
	static <T> Event<RegistryEntryAddedCallback<T>> event(Identifier registryId) {
		return RegistryEventHelper.addedCallbackEvent(registryId);
	}

	/**
	 * Get the {@link Event} for the {@link RegistryEntryAddedCallback} for the given registry.
	 *
	 * @param registry the registry to get the event for
	 * @return the event
	 */
	static <T> Event<RegistryEntryAddedCallback<T>> event(FabricRegistry<T> registry) {
		return registry.fabric$getEntryAddedCallback();
	}
}
