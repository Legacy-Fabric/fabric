package net.fabricmc.fabric.impl.event;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

@Environment(EnvType.CLIENT)
public class LegacyClientEventInvokers implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> ClientTickCallback.EVENT.invoker().tick(client));
	}
}
