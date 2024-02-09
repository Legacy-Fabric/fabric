/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.impl.registry.registries;

import com.google.common.collect.BiMap;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.registry.v1.BiomeIds;
import net.legacyfabric.fabric.impl.registry.util.ArrayAndMapBasedRegistry;

public abstract class OldBiomeRegistry extends ArrayAndMapBasedRegistry<String, Biome> {
	public OldBiomeRegistry(Biome[] valueArray, BiMap<String, Biome> defaultMap) {
		super(valueArray, defaultMap);
	}

	@Override
	public BiMap<String, String> generateOldToNewKeyMap() {
		BiMap<String, String> map = super.generateOldToNewKeyMap();

		map.put("Ocean", BiomeIds.OCEAN.toString());
		map.put("Plains", BiomeIds.PLAINS.toString());
		map.put("Desert", BiomeIds.DESERT.toString());
		map.put("Extreme Hills", BiomeIds.EXTREME_HILLS.toString());
		map.put("Forest", BiomeIds.FOREST.toString());
		map.put("Taiga", BiomeIds.TAIGA.toString());
		map.put("Swampland", BiomeIds.SWAMPLAND.toString());
		map.put("River", BiomeIds.RIVER.toString());
		map.put("Hell", BiomeIds.HELL.toString());
		map.put("The End", BiomeIds.THE_END.toString());
		map.put("FrozenOcean", BiomeIds.FROZEN_OCEAN.toString());
		map.put("FrozenRiver", BiomeIds.FROZEN_RIVER.toString());
		map.put("Ice Plains", BiomeIds.ICE_PLAINS.toString());
		map.put("Ice Mountains", BiomeIds.ICE_MOUNTAINS.toString());
		map.put("MushroomIsland", BiomeIds.MUSHROOM_ISLAND.toString());
		map.put("MushroomIslandShore", BiomeIds.MUSHROOM_ISLAND_SHORE.toString());
		map.put("Beach", BiomeIds.BEACH.toString());
		map.put("DesertHills", BiomeIds.DESERT_HILLS.toString());
		map.put("ForestHills", BiomeIds.FOREST_HILLS.toString());
		map.put("TaigaHills", BiomeIds.TAIGA_HILLS.toString());
		map.put("Extreme Hills Edge", BiomeIds.EXTREME_HILLS_EDGE.toString());
		map.put("Jungle", BiomeIds.JUNGLE.toString());
		map.put("JungleHills", BiomeIds.JUNGLE_HILLS.toString());
		map.put("JungleEdge", BiomeIds.JUNGLE_EDGE.toString());
		map.put("Deep Ocean", BiomeIds.DEEP_OCEAN.toString());
		map.put("Stone Beach", BiomeIds.STONE_BEACH.toString());
		map.put("Cold Beach", BiomeIds.COLD_BEACH.toString());
		map.put("Birch Forest", BiomeIds.BIRCH_FOREST.toString());
		map.put("Birch Forest Hills", BiomeIds.BIRCH_FOREST_HILLS.toString());
		map.put("Roofed Forest", BiomeIds.ROOFED_FOREST.toString());
		map.put("Cold Taiga", BiomeIds.COLD_TAIGA.toString());
		map.put("Cold Taiga Hills", BiomeIds.COLD_TAIGA_HILLS.toString());
		map.put("Mega Taiga", BiomeIds.MEGA_TAIGA.toString());
		map.put("Mega Taiga Hills", BiomeIds.MEGA_TAIGA_HILLS.toString());
		map.put("Extreme Hills+", BiomeIds.EXTREME_HILLS_PLUS.toString());
		map.put("Savanna", BiomeIds.SAVANNA.toString());
		map.put("Savanna Plateau", BiomeIds.SAVANNA_PLATEAU.toString());
		map.put("Mesa", BiomeIds.MESA.toString());
		map.put("Mesa Plateau F", BiomeIds.MESA_PLATEAU_F.toString());
		map.put("Mesa Plateau", BiomeIds.MESA_PLATEAU.toString());

		map.put("Sunflower Plains", BiomeIds.SUNFLOWER_PLAINS.toString());
		map.put("Desert M", BiomeIds.MUTATED_DESERT.toString());
		map.put("Extreme Hills M", BiomeIds.MUTATED_EXTREME_HILLS.toString());
		map.put("Flower Forest", BiomeIds.FLOWER_FOREST.toString());
		map.put("Taiga M", BiomeIds.MUTATED_TAIGA.toString());
		map.put("Swampland M", BiomeIds.MUTATED_SWAMPLAND.toString());
		map.put("Ice Plains Spikes", BiomeIds.ICE_PLAINS_SPIKES.toString());
		map.put("Jungle M", BiomeIds.MUTATED_JUNGLE.toString());
		map.put("JungleEdge M", BiomeIds.MUTATED_JUNGLE_EDGE.toString());
		map.put("Birch Forest M", BiomeIds.MUTATED_BIRCH_FOREST.toString());
		map.put("Birch Forest Hills M", BiomeIds.MUTATED_BIRCH_FOREST_HILLS.toString());
		map.put("Roofed Forest M", BiomeIds.MUTATED_ROOFED_FOREST.toString());
		map.put("Cold Taiga M", BiomeIds.MUTATED_COLD_TAIGA.toString());
		map.put("Mega Spruce Taiga", BiomeIds.MEGA_SPRUCE_TAIGA.toString());
		map.put("Redwood Taiga Hills M", BiomeIds.MUTATED_REDWOOD_TAIGA_HILLS.toString());
		map.put("Extreme Hills+ M", BiomeIds.MUTATED_EXTREME_HILLS_PLUS.toString());
		map.put("Savanna M", BiomeIds.MUTATED_SAVANNA.toString());
		map.put("Savanna Plateau M", BiomeIds.MUTATED_SAVANNA_PLATEAU.toString());
		map.put("Mesa (Bryce)", BiomeIds.MESA_BRYCE.toString());
		map.put("Mesa Plateau F M", BiomeIds.MUTATED_MESA_PLATEAU_F.toString());
		map.put("Mesa Plateau M", BiomeIds.MUTATED_MESA_PLATEAU.toString());

		return map;
	}

	@Override
	public KeyType getKeyType() {
		return KeyType.JAVA;
	}
}
