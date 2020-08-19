package net.fabricmc.fabric.api.biomes.v1;

import net.fabricmc.fabric.impl.biomes.InternalClimateUtils;

/**
 * A climate in the biome generator. For the vanilla overworld, but can be utilised by modded generators as well.
 */
public final class Climate {
	private static int nextId = 0;

	private final int id;

	public Climate() {
		this.id = nextId++; // Set to the next id, then increment the id counter
		InternalClimateUtils.addClimate(this, this.id);
	}

	// Must be the first climates created, in this order!
	public static final Climate OCEAN = new Climate();		// id 0
	public static final Climate DRY = new Climate();		// id 1
	public static final Climate TEMPERATE = new Climate();	// id 2
	public static final Climate COOL = new Climate();		// id 3
	public static final Climate ICY = new Climate();		// id 4
}
