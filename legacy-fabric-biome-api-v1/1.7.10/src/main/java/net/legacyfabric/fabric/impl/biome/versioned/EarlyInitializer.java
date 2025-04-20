package net.legacyfabric.fabric.impl.biome.versioned;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;

import net.legacyfabric.fabric.mixin.biome.BiomeAccessor;

import net.minecraft.world.biome.Biome;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.BIOMES).register(EarlyInitializer::biomeRegistryInit);
	}

	private static void biomeRegistryInit(Registry<?> holder) {
		SyncedRegistry<Biome> registry = (SyncedRegistry<Biome>) holder;

		registry.fabric$getEntryAddedCallback().register((rawId, id, object) -> {
			object.setName(id.toTranslationKey());
		});

		registry.fabric$getRegistryRemapCallback().register(changedIdsMap -> {
			for (RegistryEntry<Biome> entry : changedIdsMap.values()) {
				Biome biome = entry.getValue();

				if (biome != null) {
					((BiomeAccessor) biome).setId(entry.getId());
				}
			}
		});
	}
}
