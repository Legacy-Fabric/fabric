package net.fabricmc.fabric.api.biomes.v1;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.fabricmc.fabric.impl.biomes.InternalBiomeUtils;
import net.fabricmc.fabric.impl.biomes.WeightedBiomeEntry;
import net.fabricmc.fabric.impl.biomes.WeightedPicker;
import net.minecraft.world.biome.Biome;

/**
 * A climate in the biome generator. For the vanilla overworld, but can be utilised by modded generators as well.
 */
public final class Climate {
	private static int nextId = -2;

	private final int id;
	private final WeightedPicker<WeightedBiomeEntry> biomePicker = new WeightedPicker<>();

	public Climate() {
		this(null);
	}

	public Climate(@Nullable Consumer<Climate> unmoddedSetup) {
		this.id = nextId++; // Set to the next id, then increment the id counter
		InternalBiomeUtils.addClimate(this, this.id);

		if (nextId == Biome.MUSHROOM_ISLAND.field_405) { // special case in vanilla biome layers, therefore cannot use as a climate
			InternalBiomeUtils.addClimate(ISLAND, nextId++);
		}

		if (nextId == Biome.DEEP_OCEAN.field_405 || nextId == Biome.FROZEN_OCEAN.field_405) { // special case in vanilla biome layers, therefore cannot use as a climate
			InternalBiomeUtils.addClimate(OCEAN, nextId++);
		}

		if (unmoddedSetup != null) {
			unmoddedSetup.accept(this);
			this.biomePicker.setUnmodded();
		}
	}

	/**
	 * Adds the biome to this climate.
	 * @param biome the biome to add.
	 * @param weight the weight of the biome.
	 */
	public void addBiome(Biome biome, double weight) {
		InternalBiomeUtils.addBiome(this.biomePicker, biome, weight);
	}

	/**
	 * @return whether no modded biome entries exist for this climate.
	 */
	public boolean isModded() {
		return this.biomePicker.isModded();
	}

	// Must be the first climates created, in this order!
	// Hardcoded selections: ids -2 to 0
	public static final Climate ISLAND = new Climate(self -> self.addBiome(Biome.MUSHROOM_ISLAND, 1.0));
	public static final Climate FROZEN_OCEAN = new Climate(self -> self.addBiome(Biome.FROZEN_OCEAN, 1.0));
	public static final Climate OCEAN = new Climate(self -> self.addBiome(Biome.OCEAN, 1.0));
	// Vanilla Climates: ids 1 to 4
	public static final Climate DRY = new Climate(self -> {
		self.addBiome(Biome.DESERT, 3.0);
		self.addBiome(Biome.SAVANNA, 2.0);
		self.addBiome(Biome.PLAINS, 1.0);
	});
	public static final Climate TEMPERATE = new Climate(self -> {
		self.addBiome(Biome.FOREST, 1.0);
		self.addBiome(Biome.ROOFED_FOREST, 1.0);
		self.addBiome(Biome.EXTREME_HILLS, 1.0);
		self.addBiome(Biome.PLAINS, 1.0);
		self.addBiome(Biome.BIRCH_FOREST, 1.0);
		self.addBiome(Biome.SWAMPLAND, 1.0);
	});
	public static final Climate COOL = new Climate(self -> {
		self.addBiome(Biome.FOREST, 1.0);
		self.addBiome(Biome.EXTREME_HILLS, 1.0);
		self.addBiome(Biome.TAIGA, 1.0);
		self.addBiome(Biome.PLAINS, 1.0);
	});
	public static final Climate ICY = new Climate(self -> {
		self.addBiome(Biome.ICE_PLAINS, 3.0);
		self.addBiome(Biome.COLD_TAIGA, 1.0);
	});
}
