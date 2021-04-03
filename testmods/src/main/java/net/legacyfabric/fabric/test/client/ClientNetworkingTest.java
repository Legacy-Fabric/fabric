package net.legacyfabric.fabric.test.client;

import java.util.Arrays;

import net.legacyfabric.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;

public class ClientNetworkingTest implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitializeClient() {
		C2SPlayChannelEvents.REGISTER.register((handler, sender, client, channels) -> {
			LOGGER.info("Registered channels (C2S callback) - " + Arrays.toString(channels.toArray()));
		});
		C2SPlayChannelEvents.UNREGISTER.register((handler, sender, client, channels) -> {
			LOGGER.info("Unregistered channels (C2S callback) - " + Arrays.toString(channels.toArray()));
		});
		ClientPlayConnectionEvents.INIT.register((handler, client) -> {
			LOGGER.info("Connection initialized (C2S)");
		});
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			LOGGER.info("World joined (C2S)");
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			LOGGER.info("Connection disconnected (C2S)");
		});
	}
}
