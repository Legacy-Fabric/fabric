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

package net.legacyfabric.fabric.api.registry.v2.event;

import java.util.Map;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryEventHelper;

/**
 * An event for when a registry has been remapped, but hasn't been updated yet.
 *
 * @param <T> the type of entries within the registry
 */
@FunctionalInterface
public interface RegistryRemapCallback<T> {
	/**
	 * Called when the registry has been remapped but hasn't been updated yet.
	 * @param changedIdsMap A map containing changed ids in the registry following the remapping process.
	 *                      Key is the original id and Value is the affected entry data see {@link FabricRegistryEntry}.
	 */
	void callback(Map<Integer, FabricRegistryEntry<T>> changedIdsMap);

	/**
	 * Get the {@link Event} for the {@link RegistryRemapCallback} for the given registry.
	 *
	 * @param registryId the id of the registry to get the event for
	 * @return the event
	 * @throws IllegalArgumentException When the provided registry doesn't support remapping.
	 *
	 * @deprecated Use {@link #event(NamespacedIdentifier)} instead.
	 */
	@Deprecated
	static <T> Event<RegistryRemapCallback<T>> event(Identifier registryId) {
		return RegistryEventHelper.remapCallbackEvent(registryId);
	}

	/**
	 * Get the {@link Event} for the {@link RegistryRemapCallback} for the given registry.
	 *
	 * @param registryId the id of the registry to get the event for
	 * @return the event
	 * @throws IllegalArgumentException When the provided registry doesn't support remapping.
	 */
	static <T> Event<RegistryRemapCallback<T>> event(NamespacedIdentifier registryId) {
		return RegistryEventHelper.remapCallbackEvent(registryId);
	}

	/**
	 * Get the {@link Event} for the {@link RegistryRemapCallback} for the given registry.
	 *
	 * @param registry the registry to get the event for
	 * @return the event
	 * @throws IllegalArgumentException When the provided registry doesn't support remapping.
	 */
	static <T> Event<RegistryRemapCallback<T>> event(FabricRegistry<T> registry) {
		if (!(registry instanceof SyncedFabricRegistry)) throw new IllegalArgumentException("Provided registry is not remappable!");

		return ((SyncedFabricRegistry<T>) registry).fabric$getRegistryRemapCallback();
	}
}
