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

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.registry.v1.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryEntryAddedCallback;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryRemapCallback;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.util.RegistryEventsHolder;

public class BackwardCompatibilityHelper implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryHelperImplementation.registerRegisterEvent(identifier -> {
			Event<RegistryHelper.RegistryInitialized> event = RegistryHelper.IDENTIFIER_EVENT_MAP.get(identifier);

			if (event != null) {
				event.invoker().initialized();
			}
		});
	}

	private static final Map<Identifier, RegistryEventsHolder<?>> CALLBACKS = new HashMap<>();

	public static <T> RegistryEventsHolder<T> getEventHolder(Identifier identifier) {
		if (!CALLBACKS.containsKey(identifier)) {
			initCallback(identifier);
		}

		return (RegistryEventsHolder<T>) CALLBACKS.get(identifier);
	}

	private static <T> void initCallback(Identifier identifier) {
		RegistryEventsHolder<T> holder = new RegistryEventsHolder<>();
		CALLBACKS.put(identifier, holder);

		RegistryEntryAddedCallback.<T>event(identifier)
				.register(holder.getAddEvent().invoker()::onEntryAdded);
		RegistryRemapCallback.<T>event(identifier)
				.register(changedIdsMap -> {
					for (Map.Entry<Integer, RegistryEntry<T>> entry : changedIdsMap.entrySet()) {
						holder.getRemapEvent().invoker()
								.onEntryAdded(
										entry.getKey(),
										entry.getValue().getId(),
										entry.getValue().getIdentifier(),
										entry.getValue().getValue()
						);
					}
				});
	}
}
