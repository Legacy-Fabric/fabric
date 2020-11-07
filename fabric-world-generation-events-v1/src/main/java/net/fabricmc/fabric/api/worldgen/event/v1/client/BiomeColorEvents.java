package net.fabricmc.fabric.api.worldgen.event.v1.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.fabric.impl.base.util.TypedActionResult;

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
			TypedActionResult<Integer> result  = e.apply(original, world, pos, biome);

			if (result.getResult() == ActionResult.SUCCESS) {
				break;
			}
		}

		return TypedActionResult.pass(original);
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
			TypedActionResult<Integer> result  = e.apply(original, world, pos, biome);

			if (result.getResult() == ActionResult.SUCCESS) {
				break;
			}
		}

		return TypedActionResult.pass(original);
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
			TypedActionResult<Integer> result  = e.apply(original, world, pos, biome);

			if (result.getResult() == ActionResult.SUCCESS) {
				break;
			}
		}

		return TypedActionResult.pass(original);
	});

	public interface ColorEvent {
		TypedActionResult<Integer> apply(int original, WorldView world, BlockPos pos, Biome biome);
	}
}
