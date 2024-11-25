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

package net.legacyfabric.fabric.api.registry.v1;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.BackwardCompatibilityHelper;

/**
 * @deprecated Use {@link RegistryRemapCallback} instead.
 */
@Deprecated
@FunctionalInterface
public interface RegistryEntryRemapCallback<T> {
	void onEntryAdded(int oldId, int newId, Identifier key, T object);

	/**
	 * @deprecated Use {@link RegistryRemapCallback#event(Registry)} or {@link RegistryRemapCallback#event(Identifier)} instead.
	 */
	@Deprecated
	static <T> Event<RegistryEntryRemapCallback<T>> event(Identifier registryId) {
		return BackwardCompatibilityHelper.<T>getEventHolder(registryId).getRemapEvent();
	}
}
