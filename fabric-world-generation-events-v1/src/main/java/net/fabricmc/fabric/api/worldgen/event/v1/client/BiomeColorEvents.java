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

package net.fabricmc.fabric.api.worldgen.event.v1.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.IntActionResult;
import net.fabricmc.fabric.impl.base.util.ActionResult;

/**
 * Stores events that are fired when the color of foliage, water, or
 * grass is requested.
 */
public class BiomeColorEvents {
	private BiomeColorEvents() {
	}

	/**
	 * Fired when the grass color at a certain position in a certain world
	 * is requested.
	 *
	 * <p>Upon return:
	 * <ul><li>SUCCESS cancels further processing and returns the color returned.
	 * <li>Anything else returns the original color.</ul>
	 */
	public static final Event<ColorEvent> GRASS = EventFactory.createArrayBacked(ColorEvent.class, listeners -> (original, world, pos, biome) -> {
		for (ColorEvent e : listeners) {
			IntActionResult result = e.apply(original, world, pos, biome);

			if (result.getResult() == ActionResult.SUCCESS) {
				break;
			}
		}

		return IntActionResult.pass(original);
	});

	/**
	 * Fired when the foliage color at a certain position in a certain world
	 * is requested.
	 *
	 * <p>Upon return:
	 * <ul><li>SUCCESS cancels further processing and returns the color returned.
	 * <li>Anything else returns the original color.</ul>
	 */
	public static final Event<ColorEvent> FOLIAGE = EventFactory.createArrayBacked(ColorEvent.class, listeners -> (original, world, pos, biome) -> {
		for (ColorEvent e : listeners) {
			IntActionResult result = e.apply(original, world, pos, biome);

			if (result.getResult() == ActionResult.SUCCESS) {
				break;
			}
		}

		return IntActionResult.pass(original);
	});

	/**
	 * Fired when the grass color at a certain position in a certain world
	 * is requested.
	 *
	 * <p>Upon return:
	 * <ul><li>SUCCESS cancels further processing and returns the color returned.
	 * <li>Anything else returns the original color.</ul>
	 */
	public static final Event<ColorEvent> WATER = EventFactory.createArrayBacked(ColorEvent.class, listeners -> (original, world, pos, biome) -> {
		for (ColorEvent e : listeners) {
			IntActionResult result = e.apply(original, world, pos, biome);

			if (result.getResult() == ActionResult.SUCCESS) {
				break;
			}
		}

		return IntActionResult.pass(original);
	});

	public interface ColorEvent {
		IntActionResult apply(int original, WorldView world, BlockPos pos, Biome biome);
	}
}
