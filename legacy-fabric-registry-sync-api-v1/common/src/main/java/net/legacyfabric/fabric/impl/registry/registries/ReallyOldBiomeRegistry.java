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

package net.legacyfabric.fabric.impl.registry.registries;

import com.google.common.collect.BiMap;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.registry.v1.BiomeIds;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.util.ArrayBasedRegistry;

public abstract class ReallyOldBiomeRegistry extends ArrayBasedRegistry<Biome> {
	public ReallyOldBiomeRegistry(Biome[] valueArray) {
		super(valueArray);
	}

	@Override
	public BiMap<Integer, Identifier> generateIdToKeyMap() {
		BiMap<Integer, Identifier> map = super.generateIdToKeyMap();

		map.put(0, BiomeIds.OCEAN);
		map.put(1, BiomeIds.PLAINS);
		map.put(2, BiomeIds.DESERT);
		map.put(3, BiomeIds.EXTREME_HILLS);
		map.put(4, BiomeIds.FOREST);
		map.put(5, BiomeIds.TAIGA);
		map.put(6, BiomeIds.SWAMPLAND);
		map.put(7, BiomeIds.RIVER);
		map.put(8, BiomeIds.HELL);
		map.put(9, BiomeIds.THE_END);
		map.put(10, BiomeIds.FROZEN_OCEAN);
		map.put(11, BiomeIds.FROZEN_RIVER);
		map.put(12, BiomeIds.ICE_PLAINS);
		map.put(13, BiomeIds.ICE_MOUNTAINS);
		map.put(14, BiomeIds.MUSHROOM_ISLAND);
		map.put(15, BiomeIds.MUSHROOM_ISLAND_SHORE);
		map.put(16, BiomeIds.BEACH);
		map.put(17, BiomeIds.DESERT_HILLS);
		map.put(18, BiomeIds.FOREST_HILLS);
		map.put(19, BiomeIds.TAIGA_HILLS);
		map.put(20, BiomeIds.EXTREME_HILLS_EDGE);
		map.put(21, BiomeIds.JUNGLE);
		map.put(22, BiomeIds.JUNGLE_HILLS);
		map.put(23, BiomeIds.JUNGLE_EDGE);
		map.put(24, BiomeIds.DEEP_OCEAN);
		map.put(25, BiomeIds.STONE_BEACH);
		map.put(26, BiomeIds.COLD_BEACH);
		map.put(27, BiomeIds.BIRCH_FOREST);
		map.put(28, BiomeIds.BIRCH_FOREST_HILLS);
		map.put(29, BiomeIds.ROOFED_FOREST);
		map.put(30, BiomeIds.COLD_TAIGA);
		map.put(31, BiomeIds.COLD_TAIGA_HILLS);
		map.put(32, BiomeIds.MEGA_TAIGA);
		map.put(33, BiomeIds.MEGA_TAIGA_HILLS);
		map.put(34, BiomeIds.EXTREME_HILLS_PLUS);
		map.put(35, BiomeIds.SAVANNA);
		map.put(36, BiomeIds.SAVANNA_PLATEAU);
		map.put(37, BiomeIds.MESA);
		map.put(38, BiomeIds.MESA_PLATEAU_F);
		map.put(39, BiomeIds.MESA_PLATEAU);

		map.put(129, BiomeIds.SUNFLOWER_PLAINS);
		map.put(130, BiomeIds.MUTATED_DESERT);
		map.put(131, BiomeIds.MUTATED_EXTREME_HILLS);
		map.put(132, BiomeIds.FLOWER_FOREST);
		map.put(133, BiomeIds.MUTATED_TAIGA);
		map.put(134, BiomeIds.MUTATED_SWAMPLAND);
		map.put(140, BiomeIds.ICE_PLAINS_SPIKES);
		map.put(149, BiomeIds.MUTATED_JUNGLE);
		map.put(151, BiomeIds.MUTATED_JUNGLE_EDGE);
		map.put(155, BiomeIds.MUTATED_BIRCH_FOREST);
		map.put(156, BiomeIds.MUTATED_BIRCH_FOREST_HILLS);
		map.put(157, BiomeIds.MUTATED_ROOFED_FOREST);
		map.put(158, BiomeIds.MUTATED_COLD_TAIGA);
		map.put(160, BiomeIds.MEGA_SPRUCE_TAIGA);
		map.put(161, BiomeIds.MUTATED_REDWOOD_TAIGA_HILLS);
		map.put(162, BiomeIds.MUTATED_EXTREME_HILLS_PLUS);
		map.put(163, BiomeIds.MUTATED_SAVANNA);
		map.put(164, BiomeIds.MUTATED_SAVANNA_PLATEAU);
		map.put(165, BiomeIds.MESA_BRYCE);
		map.put(166, BiomeIds.MUTATED_MESA_PLATEAU_F);
		map.put(167, BiomeIds.MUTATED_MESA_PLATEAU);

		return map;
	}
}
