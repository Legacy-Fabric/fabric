package net.fabricmc.fabric.api.event.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface ServerStopCallback {
	Event<ServerStopCallback> EVENT = EventFactory.createArrayBacked(ServerStopCallback.class,
			(listeners) -> (server) -> {
				for (ServerStopCallback event : listeners) {
					event.onStopServer(server);
				}
			}
	);

	void onStopServer(MinecraftServer server);
}
