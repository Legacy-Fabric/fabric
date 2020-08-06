package net.fabricmc.fabric.impl.event;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

public class LegacyEventInvokers implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerTickEvents.END_SERVER_TICK.register(server -> ServerTickCallback.EVENT.invoker().tick(server));
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerStopCallback.EVENT.invoker().onStopServer(server));
		ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerStartCallback.EVENT.invoker().onStartServer(server));
	}
}
