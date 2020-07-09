package net.fabricmc.fabric.api.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface ClientStartCallback {

	Event<ClientStartCallback> EVENT = EventFactory.createArrayBacked(ClientStartCallback.class,
			(listeners) -> (client) -> {
				for (ClientStartCallback event : listeners) {
					event.onStartClient(client);
				}
			}
	);

	void onStartClient(MinecraftClient client);

}
