/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.fabricmc.fabric.test.worldgen;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.OreFeature;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.worldgen.event.v1.BiomeDecorationEvents;

public class WorldgenEventsTest implements ModInitializer {
	@Override
	public void onInitialize() {
		OreFeature oreFeature = new OreFeature(Blocks.DIAMOND_BLOCK.getDefaultState(), 5);
		BiomeDecorationEvents.ORE_CANCELLATION.register((cancellable, count, feature, minHeight, maxHeight, biome, world, properties) -> {
			if (biome == Biome.PLAINS && feature != oreFeature) {
				cancellable.cancel();
			}
		});
		BiomeDecorationEvents.ORE_GENERATION.register((registry, world, biome, properties) -> {
			if (biome == Biome.PLAINS) {
				registry.register(5, oreFeature, 0, 64);
			}
		});
	}
}
