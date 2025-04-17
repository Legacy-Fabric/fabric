package net.legacyfabric.fabric.impl.enchantment;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;

import net.minecraft.enchantment.Enchantment;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.ENCHANTMENTS).register(EarlyInitializer::enchantmentRegistryInit);
	}

	private static void enchantmentRegistryInit(Registry<?> holder) {
		SyncedRegistry<Enchantment> registry = (SyncedRegistry<Enchantment>) holder;

		registry.fabric$getBeforeAddedCallback().register((rawId, id, object) -> object.setName(id.toTranslationKey()));
	}
}
