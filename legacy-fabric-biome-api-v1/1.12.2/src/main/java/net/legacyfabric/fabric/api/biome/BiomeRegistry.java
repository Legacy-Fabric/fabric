package net.legacyfabric.fabric.api.biome;

import java.util.Set;

import net.legacyfabric.fabric.impl.biome.versioned.BiomeRegistryImpl;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public final class BiomeRegistry {
	public static final Registry<Biome> REGISTRY = BiomeRegistryImpl.REGISTRY;

	public static int getId(Biome biome) {
		return BiomeRegistryImpl.getId(biome);
	}

	public static NamespacedIdentifier getIdentifier(Biome biome) {
		return BiomeRegistryImpl.getIdentifier(biome);
	}

	public static ResourceKey<Biome> getKey(Biome biome) {
		return BiomeRegistryImpl.getKey(biome);
	}

	public static Biome getBiome(int id) {
		return BiomeRegistryImpl.getBiome(id);
	}

	public static Biome getBiome(NamespacedIdentifier identifier) {
		return BiomeRegistryImpl.getBiome(identifier);
	}

	public static Biome getBiome(ResourceKey<Biome> key) {
		return BiomeRegistryImpl.getBiome(key);
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return BiomeRegistryImpl.identifierSet();
	}

	public static Set<ResourceKey<Biome>> keySet() {
		return BiomeRegistryImpl.keySet();
	}

	public static <T extends Biome> T register(NamespacedIdentifier identifier, T type) {
		return BiomeRegistryImpl.register(identifier, type);
	}

	public static <T extends Biome> T register(ResourceKey<Biome> key, T type) {
		return BiomeRegistryImpl.register(key, type);
	}
}
