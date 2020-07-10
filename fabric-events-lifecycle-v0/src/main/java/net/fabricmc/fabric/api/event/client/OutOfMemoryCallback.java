package net.fabricmc.fabric.api.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface OutOfMemoryCallback {

	Event<OutOfMemoryCallback> EVENT = EventFactory.createArrayBacked(OutOfMemoryCallback.class,
			(listeners) -> (client) -> {
				for (OutOfMemoryCallback event : listeners) {
					event.onOutOfMemoryError(client);
				}
			}
	);

	void onOutOfMemoryError(MinecraftClient client);

}
