package net.legacyfabric.fabric.test;

import java.util.Arrays;

import net.legacyfabric.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.legacyfabric.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class ServerNetworkingTest implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		S2CPlayChannelEvents.REGISTER.register((handler, sender, server, channels) -> {
			LOGGER.info("Registered channels (S2C callback) - " + Arrays.toString(channels.toArray()));
		});
		S2CPlayChannelEvents.UNREGISTER.register((handler, sender, server, channels) -> {
			LOGGER.info("Unregistered channels (S2C callback) - " + Arrays.toString(channels.toArray()));
		});
		ServerPlayConnectionEvents.INIT.register((handler, server) -> {
			LOGGER.info("Connection initialized (S2C)");
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			LOGGER.info("World joined (S2C)");
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			LOGGER.info("Connection disconnected (S2C)");
		});
	}
}
