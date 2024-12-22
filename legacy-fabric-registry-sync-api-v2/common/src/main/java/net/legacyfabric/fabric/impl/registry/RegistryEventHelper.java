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

package net.legacyfabric.fabric.impl.registry;

import java.util.HashMap;
import java.util.Map;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryBeforeAddCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;

public class RegistryEventHelper {
	protected static final Map<Identifier, Event<RegistryBeforeAddCallback<?>>> IDENTIFIER_BEFORE_MAP = new HashMap<>();
	protected static final Map<Identifier, Event<RegistryEntryAddedCallback<?>>> IDENTIFIER_ADDED_MAP = new HashMap<>();
	protected static final Map<Identifier, Event<RegistryRemapCallback<?>>> IDENTIFIER_REMAP_MAP = new HashMap<>();

	public static <T> Event<RegistryBeforeAddCallback<T>> addRegistryBeforeCallback(Identifier registryId) {
		FabricRegistry<T> registry = RegistryHelperImplementation.getRegistry(registryId);

		if (registry != null) return registry.fabric$getBeforeAddedCallback();

		if (!IDENTIFIER_BEFORE_MAP.containsKey(registryId)) {
			Event<RegistryBeforeAddCallback<T>> event = EventFactory.createArrayBacked(RegistryBeforeAddCallback.class,
					(callbacks) -> (rawId, id, object) -> {
						for (RegistryBeforeAddCallback<T> callback : callbacks) {
							callback.onEntryAdding(rawId, id, object);
						}
					}
			);
			IDENTIFIER_BEFORE_MAP.put(registryId, (Event<RegistryBeforeAddCallback<?>>) (Object) event);
		}

		return (Event<RegistryBeforeAddCallback<T>>) (Object) IDENTIFIER_BEFORE_MAP.get(registryId);
	}

	public static <T> Event<RegistryEntryAddedCallback<T>> addedCallbackEvent(Identifier registryId) {
		FabricRegistry<T> registry = RegistryHelperImplementation.getRegistry(registryId);

		if (registry != null) return registry.fabric$getEntryAddedCallback();

		if (!IDENTIFIER_ADDED_MAP.containsKey(registryId)) {
			Event<RegistryEntryAddedCallback<T>> event = EventFactory.createArrayBacked(RegistryEntryAddedCallback.class,
					(callbacks) -> (rawId, id, object) -> {
						for (RegistryEntryAddedCallback<T> callback : callbacks) {
							callback.onEntryAdded(rawId, id, object);
						}
					}
			);
			IDENTIFIER_ADDED_MAP.put(registryId, (Event<RegistryEntryAddedCallback<?>>) (Object) event);
		}

		return (Event<RegistryEntryAddedCallback<T>>) (Object) IDENTIFIER_ADDED_MAP.get(registryId);
	}

	public static <T> Event<RegistryRemapCallback<T>> remapCallbackEvent(Identifier registryId) {
		FabricRegistry<T> registry = RegistryHelperImplementation.getRegistry(registryId);

		if (registry != null) return ((SyncedFabricRegistry<T>) registry).fabric$getRegistryRemapCallback();

		if (!IDENTIFIER_REMAP_MAP.containsKey(registryId)) {
			Event<RegistryRemapCallback<T>> event = EventFactory.createArrayBacked(RegistryRemapCallback.class,
					(callbacks) -> (changeMap) -> {
						for (RegistryRemapCallback<T> callback : callbacks) {
							callback.callback(changeMap);
						}
					}
			);
			IDENTIFIER_REMAP_MAP.put(registryId, (Event<RegistryRemapCallback<?>>) (Object) event);
		}

		return (Event<RegistryRemapCallback<T>>) (Object) IDENTIFIER_REMAP_MAP.get(registryId);
	}
}
