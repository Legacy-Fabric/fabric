package net.fabricmc.fabric.impl.biomes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.fabricmc.fabric.api.biomes.v1.Climate;
import net.minecraft.world.biome.Biome;

/**
 * Biome and climate related utility methods and fields for internal use only!
 */
public class InternalBiomeUtils {
	private static final List<Climate> REVERSE_CLIMATE = new ArrayList<>();
	private static final Map<Biome, Biome> DEEP_OCEANS = new HashMap<>();
	private static final Map<Biome, VariantTransformer> VARIANTS = new HashMap<>();

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

	public static void setDeepOcean(Biome original, Biome deepOcean) {
		Preconditions.checkArgument(original != null, "Orignal biome is null");
		Preconditions.checkArgument(deepOcean != null, "Deep ocean is null");
		DEEP_OCEANS.put(original, deepOcean);
	}

	public static void addVariant(Biome original, Biome variant, double chance, @Nullable Climate[] climates) {
		Preconditions.checkArgument(original != null, "Orignal biome is null");
		Preconditions.checkArgument(variant != null, "Variant is null");
		Preconditions.checkArgument(chance > 0, "Chance must be greater than zero");
		VARIANTS.computeIfAbsent(original, v -> new VariantTransformer()).addBiome(variant, chance, climates);
	}

	public static Climate getClimate(int id) {
		return REVERSE_CLIMATE.get(id);
	}

	public static Biome getDeepOcean(Biome biome) {
		return DEEP_OCEANS.getOrDefault(biome, Biome.DEEP_OCEAN);
	}

	public static Biome transformVariants(Biome biome, LayerRandom rand, Climate climate) {
		if (VARIANTS.containsKey(biome)) {
			return VARIANTS.get(biome).transformBiome(biome, rand, climate);
		} else {
			return biome;
		}
	}
}
