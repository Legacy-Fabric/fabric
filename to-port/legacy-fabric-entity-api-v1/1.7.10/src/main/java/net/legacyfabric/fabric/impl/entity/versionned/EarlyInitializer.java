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
import net.minecraft.entity.class_868;

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
			Map<Integer, class_868> newMap = Maps.newLinkedHashMap();

			for (Object entry1 : EntityType.field_3267.entrySet()) {
				Map.Entry<Object, Object> entry = (Map.Entry) entry1;
				int id = (int) entry.getKey();
				class_868 spawnEggData = (class_868) entry.getValue();

				if (changedIdsMap.containsKey(id)) {
					id = changedIdsMap.get(id).getId();
					((SpawnEggDataAccessor) spawnEggData).setId(id);
				}

				newMap.put(id, spawnEggData);
			}

			EntityType.field_3267.clear();
			EntityType.field_3267.putAll(newMap);
		});
	}
}
