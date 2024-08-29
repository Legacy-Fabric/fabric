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
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryEventHelper;

@FunctionalInterface
public interface RegistryBeforeAddCallback<T> {
	void onEntryAdding(int rawId, Identifier id, T object);

	static <T> Event<RegistryBeforeAddCallback<T>> event(Identifier registryId) {
		return RegistryEventHelper.addRegistryBeforeCallback(registryId);
	}

	static <T> Event<RegistryBeforeAddCallback<T>> event(Registry<T> registry) {
		return registry.fabric$getBeforeAddedCallback();
	}
}
