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
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.biome.BiomeEvents;

public class BiomeRegistryImpl {
	public static final ResourceKey<Registry<Biome>> KEY = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("biome");
	public static final Registry<Biome> REGISTRY = Registries.registerSimple(BiomeRegistryImpl.KEY, () -> {
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

			return Registry.register(REGISTRY, biome.id, identifier, biome);
		}
	}

	public static <T extends Biome> T register(ResourceKey<Biome> identifier, T biome) {
		if (locked) {
			throw new IllegalStateException("register called too early: registry locked!");
		} else {
			if (biome.name == null) {
				biome.setName(Util.makeTranslationKey(identifier.identifier()));
			}

			return Registry.register(REGISTRY, biome.id, identifier, biome);
		}
	}

	public static void init() {
		SyncedRegistries.register(BiomeRegistryImpl.KEY);
		SyncedRegistries.registerFixer(BiomeRegistryImpl.KEY, NamespacedIdentifiers.from("biome/id"), new BiomeIdFixer());
	}

	public static void unlock() {
		locked = false;
	}

	public static void registerBiomes() {
		VanillaBiomes.init();
		BiomeEvents.REGISTER_BIOMES.invoker().run();
	}
}
