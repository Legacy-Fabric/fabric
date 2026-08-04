package net.legacyfabric.fabric.impl.biome.versioned;

import java.util.Set;

import net.legacyfabric.fabric.api.biome.BiomeEvents;

import net.legacyfabric.fabric.mixin.biome.BiomeAccessor;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;

public class BiomeRegistryImpl {
	public static final Registry<Biome> REGISTRY = VanillaRegistries.registerSimple(VanillaRegistryKeys.BIOME, Biome.REGISTRY);

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
			if (biome.isMutated()) {
				NamespacedIdentifier parentId = NamespacedIdentifiers.from(((BiomeAccessor) biome).getParent());
				Biome parent = getBiome(parentId);
				Biome.MUTATED_BIOMES.put(biome, REGISTRY.getId(parent));
			}

			return Registry.register(REGISTRY, identifier, biome);
		}
	}

	public static <T extends Biome> T register(ResourceKey<Biome> identifier, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (biome.isMutated()) {
				NamespacedIdentifier parentId = NamespacedIdentifiers.from(((BiomeAccessor) biome).getParent());
				Biome parent = getBiome(parentId);
				Biome.MUTATED_BIOMES.put(biome, REGISTRY.getId(parent));
			}

			return Registry.register(REGISTRY, identifier, biome);
		}
	}

	public static void init() {
		SyncedRegistries.register(VanillaRegistryKeys.BIOME);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBiomes() {
		BiomeEvents.REGISTER_BIOMES.invoker().run();
	}
}
