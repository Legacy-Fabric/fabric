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

package net.fabricmc.fabric.impl.biome;

import java.util.Set;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.OreFeature;

public class OreFeatureEntry extends GenericFeatureEntry<OreFeature> {
	private final int size;
	private final int minHeight;
	private final int maxHeight;

	public OreFeatureEntry(OreFeature feature, int size, int minHeight, int maxHeight, Set<Biome> restrictionBiomes) {
		super(feature, restrictionBiomes);
		this.size = size;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}

	public int getSize() {
		return size;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}
}
