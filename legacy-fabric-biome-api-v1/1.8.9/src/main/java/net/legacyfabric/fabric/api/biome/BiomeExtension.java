package net.legacyfabric.fabric.api.biome;

import net.legacyfabric.fabric.impl.biome.versioned.BiomeRegistryImpl;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.registries.api.registry.Registry;

public interface BiomeExtension {
	Registry<Biome> BIOME_REGISTRY = BiomeRegistryImpl.REGISTRY;
	int REGISTRY_AUTO_ASSIGN_ID = -172;
}
