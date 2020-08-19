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

package net.fabricmc.fabric.impl.biomes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.fabric.api.biomes.v1.Climate;
import net.minecraft.world.biome.Biome;

/**
 * Deals with picking variants for you.
 */
final class VariantTransformer {
	private final SubTransformer defaultTransformer = new SubTransformer();
	private final Map<Climate, SubTransformer> transformers = new HashMap<>();

	/**
	 * @param variant the variant that the replaced biome is replaced with
	 * @param chance the chance of replacement of the biome into the variant
	 * @param climates the climates that the variant can replace the base biome in, empty/null indicates all climates
	 */
	void addBiome(Biome variant, double chance, Climate[] climates) {
		if (climates == null || climates.length == 0) { // no climates = all climates
			this.defaultTransformer.addBiome(variant, chance);
			// add to existing climate specific transformers
			this.transformers.forEach((climate, transformer) -> transformer.addBiome(variant, chance));
		} else {
			for (Climate climate : climates) {
				// add a climate specific transformer with all the all-climate entries by default
				this.transformers.computeIfAbsent(climate, c -> {
					SubTransformer transformer = new SubTransformer();

					for (BiomeVariant variantEntry : this.defaultTransformer.variants) {
						transformer.addBiome(variantEntry.biome, variantEntry.chance);
					}

					return transformer;
				}).addBiome(variant, chance);
			}
		}
	}

	/**
	 * Transforms a biome into a variant randomly depending on its chance.
	 *
	 * @param replaced biome to transform
	 * @param random the {@link LayerRandomnessSource} from the layer
	 * @return the transformed biome
	 */
	Biome transformBiome(Biome replaced, LayerRandom random, Climate climate) {
		SubTransformer transformer = transformers.get(climate);

		if (transformer != null) {
			return transformer.transformBiome(replaced, random);
		} else {
			return defaultTransformer.transformBiome(replaced, random);
		}
	}

	static final class SubTransformer {
		private final List<BiomeVariant> variants = new ArrayList<>();

		/**
		 * @param variant the variant that the replaced biome is replaced with
		 * @param chance the chance of replacement of the biome into the variant
		 */
		private void addBiome(Biome variant, double chance) {
			variants.add(new BiomeVariant(variant, chance));
		}

		/**
		 * Transforms a biome into a variant randomly depending on its chance.
		 *
		 * @param replaced biome to transform
		 * @param random the {@link LayerRandomnessSource} from the layer
		 * @return the transformed biome
		 */
		private Biome transformBiome(Biome replaced, LayerRandom random) {
			for (BiomeVariant variant : variants) {
				if (random.nextInt(Integer.MAX_VALUE) < variant.chance * Integer.MAX_VALUE) {
					return variant.biome;
				}
			}

			return replaced;
		}
	}

	private static class BiomeVariant {
		private final Biome biome;
		private final double chance;

		public BiomeVariant(Biome biome, double chance) {
			this.biome = biome;
			this.chance = chance;
		}
	}
}
