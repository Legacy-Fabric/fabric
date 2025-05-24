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

package net.legacyfabric.fabric.impl.entity.versionned;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.mixin.entity.SpawnEggDataAccessor;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.ENTITY_TYPES).register(EarlyInitializer::entityRegistryInit);
	}

	private static void entityRegistryInit(FabricRegistry<?> holder) {
		SyncedFabricRegistry<Class<? extends Entity>> registry = (SyncedFabricRegistry<Class<? extends Entity>>) holder;

		registry.fabric$getRegistryRemapCallback().register(changedIdsMap -> {
			Map<Integer, EntityType.SpawnEggData> newMap = Maps.newLinkedHashMap();

			for (Map.Entry<Integer, EntityType.SpawnEggData> entry : EntityType.SPAWN_EGGS.entrySet()) {
				int id = entry.getKey();
				EntityType.SpawnEggData spawnEggData = entry.getValue();

				if (changedIdsMap.containsKey(id)) {
					id = changedIdsMap.get(id).getId();
					((SpawnEggDataAccessor) spawnEggData).setId(id);
				}

				newMap.put(id, spawnEggData);
			}

			EntityType.SPAWN_EGGS.clear();
			EntityType.SPAWN_EGGS.putAll(newMap);
		});
	}
}
