/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.SinceMC;

public class BiomeIds {
	public static final Identifier OCEAN = id("ocean");
	public static final Identifier PLAINS = id("plains");
	public static final Identifier DESERT = id("desert");
	public static final Identifier EXTREME_HILLS = id("extreme_hills");
	public static final Identifier FOREST = id("forest");
	public static final Identifier TAIGA = id("taiga");
	public static final Identifier SWAMPLAND = id("swampland");
	public static final Identifier RIVER = id("river");
	public static final Identifier HELL = id("hell");
	public static final Identifier THE_END = id("sky");
	public static final Identifier FROZEN_OCEAN = id("frozen_ocean");
	public static final Identifier FROZEN_RIVER = id("frozen_river");
	public static final Identifier ICE_PLAINS = id("ice_flats");
	public static final Identifier ICE_MOUNTAINS = id("ice_mountains");
	public static final Identifier MUSHROOM_ISLAND = id("mushroom_island");
	public static final Identifier MUSHROOM_ISLAND_SHORE = id("mushroom_island_shore");
	public static final Identifier BEACH = id("beaches");
	public static final Identifier DESERT_HILLS = id("desert_hills");
	public static final Identifier FOREST_HILLS = id("forest_hills");
	public static final Identifier TAIGA_HILLS = id("taiga_hills");
	public static final Identifier EXTREME_HILLS_EDGE = id("smaller_extreme_hills");
	public static final Identifier JUNGLE = id("jungle");
	public static final Identifier JUNGLE_HILLS = id("jungle_hills");
	public static final Identifier JUNGLE_EDGE = id("jungle_edge");
	public static final Identifier DEEP_OCEAN = id("deep_ocean");
	public static final Identifier STONE_BEACH = id("stone_beach");
	public static final Identifier COLD_BEACH = id("cold_beach");
	public static final Identifier BIRCH_FOREST = id("birch_forest");
	public static final Identifier BIRCH_FOREST_HILLS = id("birch_forest_hills");
	public static final Identifier ROOFED_FOREST = id("roofed_forest");
	public static final Identifier COLD_TAIGA = id("taiga_cold");
	public static final Identifier COLD_TAIGA_HILLS = id("taiga_cold_hills");
	public static final Identifier MEGA_TAIGA = id("redwood_taiga");
	public static final Identifier MEGA_TAIGA_HILLS = id("redwood_taiga_hills");
	public static final Identifier EXTREME_HILLS_PLUS = id("extreme_hills_with_trees");
	public static final Identifier SAVANNA = id("savanna");
	public static final Identifier SAVANNA_PLATEAU = id("savanna_rock");
	public static final Identifier MESA = id("mesa");
	public static final Identifier MESA_PLATEAU_F = id("mesa_rock");
	public static final Identifier MESA_PLATEAU = id("mesa_clear_rock");
	@SinceMC("1.9")
	public static final Identifier VOID = id("void");

	public static final Identifier SUNFLOWER_PLAINS = id("mutated_plains");
	public static final Identifier MUTATED_DESERT = id("mutated_desert");
	public static final Identifier MUTATED_EXTREME_HILLS = id("mutated_extreme_hills");
	public static final Identifier FLOWER_FOREST = id("mutated_forest");
	public static final Identifier MUTATED_TAIGA = id("mutated_taiga");
	public static final Identifier MUTATED_SWAMPLAND = id("mutated_swampland");
	public static final Identifier ICE_PLAINS_SPIKES = id("mutated_ice_flats");
	public static final Identifier MUTATED_JUNGLE = id("mutated_jungle");
	public static final Identifier MUTATED_JUNGLE_EDGE = id("mutated_jungle_edge");
	public static final Identifier MUTATED_BIRCH_FOREST = id("mutated_birch_forest");
	/**
	 * [1.9-1.10.2]: parent is BIRCH_FOREST.
	 * Pre-1.9 and 1.11+: parent is BIRCH_FOREST_HILLS.
	 */
	public static final Identifier MUTATED_BIRCH_FOREST_HILLS = id("mutated_birch_forest_hills");
	public static final Identifier MUTATED_ROOFED_FOREST = id("mutated_roofed_forest");
	public static final Identifier MUTATED_COLD_TAIGA = id("mutated_taiga_cold");
	public static final Identifier MEGA_SPRUCE_TAIGA = id("mutated_redwood_taiga");
	/**
	 * Is the same as MUTATED_MEGA_TAIGA before 1.8.
	 */
	public static final Identifier MUTATED_REDWOOD_TAIGA_HILLS = id("mutated_redwood_taiga_hills");
	public static final Identifier MUTATED_EXTREME_HILLS_PLUS = id("mutated_extreme_hills_with_trees");
	public static final Identifier MUTATED_SAVANNA = id("mutated_savanna");
	public static final Identifier MUTATED_SAVANNA_PLATEAU = id("mutated_savanna_rock");
	public static final Identifier MESA_BRYCE = id("mutated_mesa");
	public static final Identifier MUTATED_MESA_PLATEAU_F = id("mutated_mesa_rock");
	public static final Identifier MUTATED_MESA_PLATEAU = id("mutated_mesa_clear_rock");

	private static Identifier id(String path) {
		return new Identifier(path);
	}
}
