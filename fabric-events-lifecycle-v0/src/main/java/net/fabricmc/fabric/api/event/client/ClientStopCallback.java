package net.fabricmc.fabric.api.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface ClientStopCallback {
	Event<ClientStopCallback> EVENT = EventFactory.createArrayBacked(ClientStopCallback.class,
			(listeners) -> (client) -> {
				for (ClientStopCallback event : listeners) {
					event.onStopClient(client);
				}
			}
	);

	void onStopClient(MinecraftClient client);
}
