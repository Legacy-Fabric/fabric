package net.legacyfabric.fabric.test.client;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityType;

import net.fabricmc.api.ClientModInitializer;

public class ClientLifecycleEventsTest implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitializeClient() {
		ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.info("Client chunk loaded at {} {}", chunk.chunkZ, chunk.chunkZ);
			}
		});
		ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.info("Client chunk unloaded at {} {}", chunk.chunkZ, chunk.chunkZ);
			}
		});
		ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			LOGGER.info("Client Entity {} loaded", EntityType.getEntityName(entity));
		});
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			LOGGER.info("Client Entity {} unloaded", EntityType.getEntityName(entity));
		});
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			LOGGER.info("Client started");
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			LOGGER.info("Client stopping");
		});
	}
}
