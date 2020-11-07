package net.fabricmc.fabric.api.worldgen.event.v1;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.CustomizedWorldProperties;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Stores events which relate to biome decoration.
 */
public class BiomeDecorationEvents {
	private BiomeDecorationEvents() {
	}

	/**
	 * Fired after all vanilla ores have finished generating.
	 */
	public static final Event<OreRegistrationConsumer> ORE_GENERATION = EventFactory.createArrayBacked(OreRegistrationConsumer.class, listeners -> (registry, world, biome, properties) -> {
		for (OreRegistrationConsumer c : listeners) {
			c.accept(registry, world, biome, properties);
		}
	});

	@FunctionalInterface
	public interface OreRegistrationConsumer {
		void accept(OreRegistry registry, World world, Biome biome, CustomizedWorldProperties properties);
	}
}
