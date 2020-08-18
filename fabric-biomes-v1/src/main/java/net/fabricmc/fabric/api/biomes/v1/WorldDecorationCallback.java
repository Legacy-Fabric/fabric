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

package net.fabricmc.fabric.api.biomes.v1;

import java.util.Random;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;

/**
 * A hook into world decoration to add custom features and other post-generation tweaks.
 */
@FunctionalInterface
public interface WorldDecorationCallback {
	Event<WorldDecorationCallback> EVENT = EventFactory.createArrayBacked(WorldDecorationCallback.class, listeners -> (world, chunkProvider, rand, pos) -> {
		for (WorldDecorationCallback listener : listeners) {
			listener.onWorldDecorate(world, chunkProvider, rand, pos);
		}
	});

	/**
	 * Called when the world is being decorated, after vanilla decoration.
	 * @param world the world being decorated.
	 * @param chunkProvider the chunk provider decorating the world.
	 * @param rand the random instance for world gen.
	 * @param pos the start block position for generation. (pos.x + 8, pos.z + 8) is usually used as the lowest start position for feature generation.
	 */
	void onWorldDecorate(World world, ChunkProvider chunkProvider, Random rand, BlockPos pos);
}
