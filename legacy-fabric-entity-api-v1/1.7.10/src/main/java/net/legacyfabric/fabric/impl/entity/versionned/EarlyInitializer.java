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
import net.minecraft.entity.class_868;

import java.util.Map;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.ENTITY_TYPES).register(EarlyInitializer::entityRegistryInit);
	}

	private static void entityRegistryInit(Registry<?> holder) {
		SyncedRegistry<Class<? extends Entity>> registry = (SyncedRegistry<Class<? extends Entity>>) holder;

		registry.fabric$getRegistryRemapCallback().register(changedIdsMap -> {
			Map<Integer, class_868> newMap = Maps.newLinkedHashMap();

			for (Object entry1 : EntityType.field_3267.entrySet()) {
				Map.Entry<Object, Object> entry = (Map.Entry) entry1;
				int id = (int) entry.getKey();
				class_868 spawnEggData = (class_868) entry.getValue();

				if (changedIdsMap.containsKey(id)) {
					id = changedIdsMap.get(id).getId();
					((SpawnEggDataAccessor) spawnEggData).setField_3273(id);
				}

				newMap.put(id, spawnEggData);
			}

			EntityType.field_3267.clear();
			EntityType.field_3267.putAll(newMap);
		});
	}
}
