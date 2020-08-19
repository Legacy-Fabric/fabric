package net.fabricmc.fabric.impl.biomes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import net.fabricmc.fabric.api.biomes.v1.Climate;
import net.minecraft.world.biome.Biome;

/**
 * Biome and climate related utility methods and fields for internal use only!
 */
public class InternalBiomeUtils {
	private static final List<Climate> REVERSE_CLIMATE = new ArrayList<>();

	public static void addClimate(Climate climate, int id) {
		Preconditions.checkArgument(id == REVERSE_CLIMATE.size(), "Given climate id already exists");
		Preconditions.checkArgument(climate != null, "Climate is null");
		REVERSE_CLIMATE.add(climate);
	}

	public static void addBiome(WeightedPicker<WeightedBiomeEntry> biomePicker, Biome biome, double weight) {
		Preconditions.checkArgument(biome != null, "Biome is null");
		Preconditions.checkArgument(weight > 0, "Weight must be greater than zero");
		biomePicker.add(new WeightedBiomeEntry(biome, weight));
	}
}
