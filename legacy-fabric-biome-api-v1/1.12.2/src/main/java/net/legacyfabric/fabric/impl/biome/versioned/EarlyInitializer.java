package net.legacyfabric.fabric.impl.biome.versioned;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;

import net.legacyfabric.fabric.api.registry.v2.registry.registrable.IdsHolder;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.mixin.biome.BiomeAccessor;

import net.minecraft.util.collection.IdList;
import net.minecraft.world.biome.Biome;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.BIOMES).register(EarlyInitializer::biomeRegistryInit);
	}

	private static void biomeRegistryInit(Registry<?> holder) {
		SyncedRegistry<Biome> registry = (SyncedRegistry<Biome>) holder;

		registry.fabric$getBeforeAddedCallback().register((rawId, id, object) -> {
			((BiomeAccessor) object).setName(id.toTranslationKey());
		});

		registry.fabric$getEntryAddedCallback().register((rawId, id, object) -> {
			if (object.hasParent()) {
				Biome.biomeList.set(object, registry.fabric$getRawId(
						new Identifier(((BiomeAccessor) object).getParent())
				));
			}
		});

		registry.fabric$getRegistryRemapCallback().register(changedIdsMap -> {
			IdList<Biome> list = (IdList<Biome>) Biome.biomeList.fabric$new();

			for (Biome biome : (IdsHolder<Biome>) Biome.biomeList) {
				if (biome == null) continue;

				int index = Biome.biomeList.fabric$getId(biome);

				if (changedIdsMap.containsKey(index)) {
					index = changedIdsMap.get(index).getId();
				}

				list.set(biome, index);
			}

			BiomeAccessor.setBiomeList(list);
		});
	}
}
