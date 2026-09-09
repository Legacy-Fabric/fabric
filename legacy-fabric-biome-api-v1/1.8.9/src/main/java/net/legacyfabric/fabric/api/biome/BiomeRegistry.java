/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.api.biome;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.impl.biome.versioned.BiomeRegistryImpl;

public final class BiomeRegistry {
	public static final ResourceKey<Registry<Biome>> KEY = BiomeRegistryImpl.KEY;
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
