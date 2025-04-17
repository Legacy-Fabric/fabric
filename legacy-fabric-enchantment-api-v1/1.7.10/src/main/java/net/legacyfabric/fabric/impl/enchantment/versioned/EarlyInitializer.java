package net.legacyfabric.fabric.impl.enchantment.versioned;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.enchantment.EnchantmentIds;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.RegistryEntry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedRegistry;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.mixin.enchantment.EnchantmentAccessor;

import net.minecraft.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Map;

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

	public static Map<Integer, Identifier> getVanillaIds() {
		Map<Integer, Identifier> map = new HashMap<>();

		map.put(0, EnchantmentIds.PROTECTION);
		map.put(1, EnchantmentIds.FIRE_PROTECTION);
		map.put(2, EnchantmentIds.FEATHER_FALLING);
		map.put(3, EnchantmentIds.BLAST_PROTECTION);
		map.put(4, EnchantmentIds.PROJECTILE_PROTECTION);
		map.put(5, EnchantmentIds.RESPIRATION);
		map.put(6, EnchantmentIds.AQUA_AFFINITY);
		map.put(7, EnchantmentIds.THORNS);
		map.put(16, EnchantmentIds.SHARPNESS);
		map.put(17, EnchantmentIds.SMITE);
		map.put(18, EnchantmentIds.BANE_OF_ARTHROPODS);
		map.put(19, EnchantmentIds.KNOCKBACK);
		map.put(20, EnchantmentIds.FIRE_ASPECT);
		map.put(21, EnchantmentIds.LOOTING);
		map.put(32, EnchantmentIds.EFFICIENCY);
		map.put(33, EnchantmentIds.SILK_TOUCH);
		map.put(34, EnchantmentIds.UNBREAKING);
		map.put(35, EnchantmentIds.FORTUNE);
		map.put(48, EnchantmentIds.POWER);
		map.put(49, EnchantmentIds.PUNCH);
		map.put(50, EnchantmentIds.FLAME);
		map.put(51, EnchantmentIds.INFINITY);
		map.put(61, EnchantmentIds.LUCK_OF_THE_SEA);
		map.put(62, EnchantmentIds.LURE);

		return map;
	}
}
