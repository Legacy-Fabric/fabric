package net.fabricmc.fabric.impl.event;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.server.DedicatedServerSetupCallback;
import net.fabricmc.fabric.api.server.event.lifecycle.v1.DedicatedServerLifecycleEvents;

@Environment(EnvType.SERVER)
public class LegacyServerEventInvokers implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		DedicatedServerLifecycleEvents.POST_SETUP.register(server -> DedicatedServerSetupCallback.EVENT.invoker().onServerSetup(server));
	}
}
