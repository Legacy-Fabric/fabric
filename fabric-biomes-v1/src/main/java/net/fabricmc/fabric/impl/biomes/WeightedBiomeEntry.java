package net.fabricmc.fabric.impl.biomes;

import net.minecraft.world.biome.Biome;

import net.fabricmc.fabric.impl.biomes.WeightedPicker.WeightedEntry;

/**
 * Entry of a biome and its weight.
 */
public final class WeightedBiomeEntry implements WeightedEntry {
	private final Biome biome;
	private final double weight;
	private double upperWeightBound;

	/**
	 * @param biome  the biome
	 * @param weight how often a biome will be chosen
	 */
	WeightedBiomeEntry(final Biome biome, final double weight) {
		this.biome = biome;
		this.weight = weight;
	}

	public Biome getBiome() {
		return this.biome;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * @param upperWeightBound the upper weight bound within the context of the other entries, used for the binary search.
	 */
	@Override
	public void updateUpperWeightBound(double upperWeightBound) {
		this.upperWeightBound = upperWeightBound;
	}

	/**
	 * @return the upper weight boundary for the search
	 */
	@Override
	public double getUpperWeightBound() {
		return this.upperWeightBound;
	}
}
