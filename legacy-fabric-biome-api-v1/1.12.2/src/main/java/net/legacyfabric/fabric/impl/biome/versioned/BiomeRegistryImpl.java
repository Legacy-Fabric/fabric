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

package net.legacyfabric.fabric.impl.biome.versioned;

import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.impl.registry.VanillaRegistries;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.biome.BiomeEvents;
import net.legacyfabric.fabric.mixin.biome.BiomeAccessor;

public class BiomeRegistryImpl {
	public static final ResourceKey<Registry<Biome>> KEY = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("biome");
	public static final Registry<Biome> REGISTRY = VanillaRegistries.registerSimple(BiomeRegistryImpl.KEY, Biome.REGISTRY);

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
		SyncedRegistries.register(BiomeRegistryImpl.KEY);
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBiomes() {
		BiomeEvents.REGISTER_BIOMES.invoker().run();
	}
}
