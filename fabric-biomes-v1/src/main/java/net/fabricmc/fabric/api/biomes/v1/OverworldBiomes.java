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

import net.minecraft.world.biome.Biome;

import net.fabricmc.fabric.impl.biomes.InternalBiomeUtils;

/**
 * API that exposes some internals of the default overworld biome source.
 */
public final class OverworldBiomes {
	private OverworldBiomes() {
	}

	/**
	 * Sets the deep ocean variant of this biome.
	 *
	 * @param original  the original ocean biome.
	 * @param deepOcean the deep ocean variant of this biome.
	 */
	public static void setDeepOcean(Biome original, Biome deepOcean) {
		InternalBiomeUtils.setDeepOcean(original, deepOcean);
	}

	/**
	 * Sets the deep ocean variant of this biome.
	 *
	 * @param original  the original ocean biome.
	 * @param variant the deep ocean variant of this biome.
	 * @param chance the chance
	 * @param climates the climates for the variant to apply in. If none are given, applies to every climate.
	 */
	public static void addVariant(Biome original, Biome variant, double chance, Climate... climates) {
		InternalBiomeUtils.addVariant(original, variant, chance, climates);
	}
}
