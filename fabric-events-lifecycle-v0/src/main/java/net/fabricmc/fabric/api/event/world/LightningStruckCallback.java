package net.fabricmc.fabric.api.event.world;

import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

@Deprecated
public interface LightningStruckCallback {
	/**
	 * @deprecated Please use {@link ServerEntityEvents#LIGHTNING_STRIKE}
	 */
	@Deprecated
	Event<LightningStruckCallback> EVENT = EventFactory.createArrayBacked(LightningStruckCallback.class, (listeners) -> (world, x, y, z) -> {
		for (LightningStruckCallback callback : listeners) {
			callback.onLightningStrike(world, x, y, z);
		}
	});

	void onLightningStrike(World world, double x, double y, double z);
}
