package net.legacyfabric.fabric.impl.biome.versioned;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.biome.BiomeEvents;
import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;

public class BiomeRegistryImpl {
	public static final Registry<Biome> REGISTRY = Registries.registerSimple(VanillaRegistryKeys.BIOME, () -> {
		Biome.getAll();
	});

	private static boolean locked = true;

	public static int getId(Biome biome) {
		return REGISTRY.getId(biome);
	}

	public static NamespacedIdentifier getIdentifier(Biome biome) {
		return REGISTRY.getIdentifier(biome);
	}

	public static ResourceKey<Biome> getKey(Biome biome) {
		return REGISTRY.getKey(biome);
	}

	public static Biome getBiome(int id) {
		return REGISTRY.get(id);
	}

	public static Biome getBiome(NamespacedIdentifier identifier) {
		return REGISTRY.get(identifier);
	}

	public static Biome getBiome(ResourceKey<Biome> key) {
		return REGISTRY.get(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return REGISTRY.identifierSet();
	}

	public static Set<ResourceKey<Biome>> keySet() {
		return REGISTRY.keySet();
	}

	public static <T extends Biome> T register(NamespacedIdentifier identifier, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (biome.name == null) {
				biome.setName(Util.makeTranslationKey(identifier));
			}

			return Registry.register(REGISTRY, identifier, biome);
		}
	}

	public static <T extends Biome> T register(ResourceKey<Biome> identifier, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (biome.name == null) {
				biome.setName(Util.makeTranslationKey(identifier.identifier()));
			}

			return Registry.register(REGISTRY, identifier, biome);
		}
	}

	public static void init() {
		SyncedRegistries.register(VanillaRegistryKeys.BIOME);
		SyncedRegistries.registerFixer(VanillaRegistryKeys.BIOME, NamespacedIdentifiers.from("biome/id"), new BiomeIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBiomes() {
		VanillaBiomes.init();
		BiomeEvents.REGISTER_BIOMES.invoker().run();
	}
}
