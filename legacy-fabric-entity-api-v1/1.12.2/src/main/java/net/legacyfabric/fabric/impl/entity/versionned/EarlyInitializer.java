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

package net.legacyfabric.fabric.impl.entity.versionned;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.mixin.entity.EntityTypeAccessor;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.ENTITY_TYPES).register(EarlyInitializer::entityRegistryInit);
	}

	private static void entityRegistryInit(FabricRegistry<?> holder) {
		SyncedFabricRegistry<Class<? extends Entity>> registry = (SyncedFabricRegistry<Class<? extends Entity>>) holder;

		registry.fabric$getEntryAddedCallback().register((rawId, id, object) -> {
			EntityType.IDENTIFIERS.add(new Identifier(id.toString()));

			while (rawId >= EntityTypeAccessor.getEntityNameList().size()) {
				EntityTypeAccessor.getEntityNameList().add(null);
			}

			EntityTypeAccessor.getEntityNameList().set(rawId, id.toTranslationKey());
		});

		registry.fabric$getRegistryRemapCallback().register(changedIdsMap -> {
			List<String> oldList = EntityTypeAccessor.getEntityNameList();
			List<String> newList = new ArrayList<>();

			for (int i = 0; i < oldList.size(); i++) {
				int id = i;
				String name = oldList.get(i);

				if (name == null) continue;

				if (changedIdsMap.containsKey(id)) {
					id = changedIdsMap.get(id).getId();
				}

				while (id >= newList.size()) {
					newList.add(null);
				}

				newList.set(id, name);
			}

			EntityTypeAccessor.setEntityNameList(newList);
		});
	}
}
