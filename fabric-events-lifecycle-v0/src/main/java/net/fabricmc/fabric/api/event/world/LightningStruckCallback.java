package net.fabricmc.fabric.api.event.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.World;

public interface LightningStruckCallback {

	Event<LightningStruckCallback> EVENT = EventFactory.createArrayBacked(LightningStruckCallback.class, (listeners) -> (world, x, y, z) -> {
		for (LightningStruckCallback callback : listeners) {
			callback.onLightningStrike(world,x,y,z);
		}
	});

	void onLightningStrike(World world , double x, double y, double z);

}
