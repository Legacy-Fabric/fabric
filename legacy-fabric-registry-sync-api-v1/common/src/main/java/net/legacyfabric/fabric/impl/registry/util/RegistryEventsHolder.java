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

package net.legacyfabric.fabric.impl.registry.util;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.legacyfabric.fabric.api.registry.v1.RegistryEntryAddCallback;
import net.legacyfabric.fabric.api.registry.v1.RegistryEntryRemapCallback;

@Deprecated
public class RegistryEventsHolder<V> {
	private final Event<RegistryEntryAddCallback<V>> ADD_EVENT = EventFactory.createArrayBacked(RegistryEntryAddCallback.class,
			(callbacks) -> (rawId, id, object) -> {
				for (RegistryEntryAddCallback<V> callback : callbacks) {
					callback.onEntryAdded(rawId, id, object);
				}
			}
	);

	private final Event<RegistryEntryRemapCallback<V>> REMAP_EVENT = EventFactory.createArrayBacked(RegistryEntryRemapCallback.class,
			(callbacks) -> (oldId, newId, id, object) -> {
				for (RegistryEntryRemapCallback<V> callback : callbacks) {
					callback.onEntryAdded(oldId, newId, id, object);
				}
			}
	);

	public Event<RegistryEntryAddCallback<V>> getAddEvent() {
		return ADD_EVENT;
	}

	public Event<RegistryEntryRemapCallback<V>> getRemapEvent() {
		return REMAP_EVENT;
	}
}
