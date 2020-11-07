package net.fabricmc.fabric.api.worldgen.event.v1;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OreFeature;

/**
 * A registry that stores a list of ore entries.
 */
public interface OreRegistry {
	/**
	 * Adds a new entry to this ore registry.
	 *
	 * @param count the number of times {@link OreFeature#generate(World, Random, BlockPos)} should be called
	 * @param feature the feature
	 * @param minHeight the minimum height that the feature should generate above
	 * @param maxHeight the maximum height that the feature should generate below
	 */
	void register(int count, OreFeature feature, int minHeight, int maxHeight);
}
