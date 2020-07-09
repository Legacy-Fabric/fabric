package net.fabricmc.fabric.api.event.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public interface DedicatedServerSetupCallback {

	Event<DedicatedServerSetupCallback> EVENT = EventFactory.createArrayBacked(DedicatedServerSetupCallback.class,
			(listeners) -> (server) -> {
				for (DedicatedServerSetupCallback event : listeners) {
					event.onServerSetup(server);
				}
			}
	);

	void onServerSetup(MinecraftDedicatedServer server);

}
