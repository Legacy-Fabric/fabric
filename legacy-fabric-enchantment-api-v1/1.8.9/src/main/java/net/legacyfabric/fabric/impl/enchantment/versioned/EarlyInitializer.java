package net.legacyfabric.fabric.impl.enchantment.versioned;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;

import net.legacyfabric.fabric.mixin.enchantment.EnchantmentAccessor;

import net.minecraft.enchantment.Enchantment;

public class EarlyInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.ENCHANTMENTS).register(EarlyInitializer::enchantmentRegistryInit);
	}

	private static void enchantmentRegistryInit(Registry<?> holder) {
		SyncedRegistry<Enchantment> registry = (SyncedRegistry<Enchantment>) holder;

		registry.fabric$getRegistryRemapCallback().register(changedIdsMap -> {
			for (RegistryEntry<Enchantment> entry : changedIdsMap.values()) {
				Enchantment enchantment = entry.getValue();

				if (enchantment != null) {
					((EnchantmentAccessor) enchantment).setId(entry.getId());
				}
			}
		});
	}
}
