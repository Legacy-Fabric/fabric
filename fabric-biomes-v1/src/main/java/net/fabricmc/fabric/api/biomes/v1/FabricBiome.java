/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.biomes.v1;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;

/**
 * Fabric-provided extensions to the biome class' functionality. Implement this on your {@linkplain Biome biome} classes to add the provided functionality.
 * It is recommended that custom chunk generators and features check these biome properties as well, in order to provide maximum inter-mod compatability.
 */
public interface FabricBiome {
	/**
	 * @param lakeBlock the fluid block the lake is made out of.
	 * @param rand      the world gen random. Useful if you want to decrease the chance of a lake, but not eliminate it completely.
	 * @return whether the lake is allowed to generate, for this biome.
	 */
	default boolean isLakeAllowed(Block lakeBlock, Random rand) {
		return true;
	}
}
