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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;

import net.minecraft.world.biome.Biome;

final class VanillaBiomes {
	private static final String[] IDENTIFIERS = {
			"ocean",
			"plains",
			"desert",
			"extreme_hills",
			"forest",
			"taiga",
			"swampland",
			"river",
			"hell",
			"sky",
			"frozen_ocean",
			"frozen_river",
			"ice_flats",
			"ice_mountains",
			"mushroom_island",
			"mushroom_island_shore",
			"beaches",
			"desert_hills",
			"forest_hills",
			"taiga_hills",
			"smaller_extreme_hills",
			"jungle",
			"jungle_hills",
			"jungle_edge",
			"deep_ocean",
			"stone_beach",
			"cold_beach",
			"birch_forest",
			"birch_forest_hills",
			"roofed_forest",
			"taiga_cold",
			"taiga_cold_hills",
			"redwood_taiga",
			"redwood_taiga_hills",
			"extreme_hills_with_trees",
			"savanna",
			"savanna_rock",
			"mesa",
			"mesa_rock",
			"mesa_clear_rock",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			"mutated_plains",
			"mutated_desert",
			"mutated_extreme_hills",
			"mutated_forest",
			"mutated_taiga",
			"mutated_swampland",
			null,
			null,
			null,
			null,
			null,
			"mutated_ice_flats",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			"mutated_jungle",
			null,
			"mutated_jungle_edge",
			null,
			null,
			null,
			"mutated_birch_forest",
			"mutated_birch_forest_hills",
			"mutated_roofed_forest",
			"mutated_taiga_cold",
			null,
			"mutated_redwood_taiga",
			"mutated_redwood_taiga_hills",
			"mutated_extreme_hills_with_trees",
			"mutated_savanna",
			"mutated_savanna_rock",
			"mutated_mesa",
			"mutated_mesa_rock",
			"mutated_mesa_clear_rock"
	};

	static void init() {
		for (Field f : Biome.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers()) && Biome.class.isAssignableFrom(f.getType())) {
				try {
					Biome biome = (Biome) f.get(null);

					if (biome != null) {
						register(biome);
					}
				} catch (Throwable ignored) {
					// ignored
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void register(Biome biome) {
		if (biome.id >= 0 && biome.id < IDENTIFIERS.length) {
			String identifier = IDENTIFIERS[biome.id];

			if (identifier != null) {
				Registry.register(BiomeRegistryImpl.REGISTRY, biome.id, NamespacedIdentifiers.from(identifier), biome);
			}
		}
	}
}
