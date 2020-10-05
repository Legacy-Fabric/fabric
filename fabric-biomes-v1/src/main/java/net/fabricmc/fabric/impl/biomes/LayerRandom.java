package net.fabricmc.fabric.impl.biomes;

/**
 * Interface for invoking the protected method nextInt() on various layers.
 */
public interface LayerRandom {
	int nextInt(int bound);
}
