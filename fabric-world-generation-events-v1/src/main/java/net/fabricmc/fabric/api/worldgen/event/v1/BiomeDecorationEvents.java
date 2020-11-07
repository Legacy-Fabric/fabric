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

import org.spongepowered.asm.mixin.injection.callback.Cancellable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.gen.CustomizedWorldProperties;
import net.minecraft.world.gen.feature.OreFeature;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Stores events which relate to biome decoration.
 */
public class BiomeDecorationEvents {
	private BiomeDecorationEvents() {
	}

	/**
	 * The event for the world decoration generation stage, where features such as trees, ores and lakes are introduced.
	 */
	public static final Event<WorldDecorate> WORLD_DECORATION = EventFactory.createArrayBacked(WorldDecorate.class, listeners -> (world, chunkProvider, rand, pos) -> {
		for (WorldDecorate listener : listeners) {
			listener.onWorldDecorate(world, chunkProvider, rand, pos);
		}
	});


	/**
	 * Fired after all vanilla ores have finished generating.
	 */
	public static final Event<OreRegistrationConsumer> ORE_GENERATION = EventFactory.createArrayBacked(OreRegistrationConsumer.class, listeners -> (registry, world, biome, properties) -> {
		for (OreRegistrationConsumer c : listeners) {
			c.accept(registry, world, biome, properties);
		}
	});

	/**
	 * Allows cancelling the generation of a certain ore.
	 */
	public static final Event<OreCancellationConsumer> ORE_CANCELLATION = EventFactory.createArrayBacked(OreCancellationConsumer.class, listeners -> (cancellable, count, feature, minHeight, maxHeight, biome, world, properties) -> {
		for (OreCancellationConsumer c : listeners) {
			c.accept(cancellable, count, feature, minHeight, maxHeight, biome, world, properties);

			if (cancellable.isCancelled()) {
				break;
			}
 		}
	});

	@FunctionalInterface
	public interface OreRegistrationConsumer {
		void accept(OreRegistry registry, World world, Biome biome, CustomizedWorldProperties properties);
	}

	@FunctionalInterface
	public interface OreCancellationConsumer {
		void accept(Cancellable cancellable, int count, OreFeature feature, int minHeight, int maxHeight, Biome biome, World world, CustomizedWorldProperties properties);
	}

	/**
	 * A hook into world decoration to add custom features and other post-generation tweaks.
	 */
	@FunctionalInterface
	public interface WorldDecorate {
		/**
		 * Called when the world is being decorated, after vanilla decoration.
		 *
		 * @param world the world being decorated.
		 * @param chunkProvider the chunk provider decorating the world.
		 * @param rand the random instance for world gen.
		 * @param pos the start block position for generation. (pos.x + 8, pos.z + 8) is usually used as the lowest start position for feature generation.
		 */
		void onWorldDecorate(World world, ChunkProvider chunkProvider, Random rand, BlockPos pos);
	}
}
