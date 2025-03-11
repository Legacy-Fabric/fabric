package net.legacyfabric.fabric.impl.entity.versionned;

import com.google.common.collect.Maps;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;

import net.legacyfabric.fabric.mixin.entity.SpawnEggDataAccessor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.Map;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.ENTITY_TYPES).register(EarlyInitializer::entityRegistryInit);
	}

	private static void entityRegistryInit(Registry<?> holder) {
		SyncedRegistry<Class<? extends Entity>> registry = (SyncedRegistry<Class<? extends Entity>>) holder;

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
