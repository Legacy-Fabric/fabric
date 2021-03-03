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

package net.fabricmc.fabric.api.worldgen.event.v1;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OreFeature;

/**
 * A registry that stores a list of ore entries.
 */
public interface OreRegistry extends Iterable<OreEntry> {
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
