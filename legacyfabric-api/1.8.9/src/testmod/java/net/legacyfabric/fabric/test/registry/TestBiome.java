package net.legacyfabric.fabric.test.registry;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MutatedBiome;
import net.minecraft.world.biome.PlainsBiome;

public class TestBiome extends PlainsBiome {
	protected TestBiome() {
		super(REGISTRY_AUTO_ASSIGN_ID);
	}

	@Override
	public Biome mutate(int id) {
		return new MutatedBiome(id, this);
	}
}
