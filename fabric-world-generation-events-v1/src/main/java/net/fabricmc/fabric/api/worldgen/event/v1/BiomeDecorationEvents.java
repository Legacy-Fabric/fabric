package net.fabricmc.fabric.api.worldgen.event.v1;

import org.spongepowered.asm.mixin.injection.callback.Cancellable;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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
}
