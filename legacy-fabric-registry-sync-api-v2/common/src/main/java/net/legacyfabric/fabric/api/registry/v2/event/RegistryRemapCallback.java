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

import java.util.Map;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryEventHelper;

@FunctionalInterface
public interface RegistryRemapCallback<T> {
	void callback(Map<Integer, FabricRegistryEntry<T>> changedIdsMap);

	static <T> Event<RegistryRemapCallback<T>> event(Identifier registryId) {
		return RegistryEventHelper.remapCallbackEvent(registryId);
	}

	static <T> Event<RegistryRemapCallback<T>> event(FabricRegistry<T> registry) {
		if (!(registry instanceof SyncedFabricRegistry)) throw new IllegalArgumentException("Provided registry is not remappable!");

		return ((SyncedFabricRegistry<T>) registry).fabric$getRegistryRemapCallback();
	}
}
