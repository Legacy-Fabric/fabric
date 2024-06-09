package net.legacyfabric.fabric.impl.entity.versionned;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;

import net.legacyfabric.fabric.mixin.entity.EntityTypeAccessor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.ENTITY_TYPES).register(EarlyInitializer::entityRegistryInit);
	}

	private static void entityRegistryInit(Registry<?> holder) {
		SyncedRegistry<Class<? extends Entity>> registry = (SyncedRegistry<Class<? extends Entity>>) holder;

		registry.fabric$getEntryAddedCallback().register((rawId, id, object) -> {
			EntityType.IDENTIFIERS.add(new Identifier(id.toString()));

			while (rawId >= EntityTypeAccessor.getNAMES().size()) {
				EntityTypeAccessor.getNAMES().add(null);
			}

			EntityTypeAccessor.getNAMES().set(rawId, id.toTranslationKey());
		});

		registry.fabric$getRegistryRemapCallback().register(changedIdsMap -> {
			List<String> oldList = EntityTypeAccessor.getNAMES();
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

			EntityTypeAccessor.setNAMES(newList);
		});
	}
}
