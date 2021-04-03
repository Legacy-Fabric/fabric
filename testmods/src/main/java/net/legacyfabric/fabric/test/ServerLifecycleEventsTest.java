package net.legacyfabric.fabric.test;

import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityType;

import net.fabricmc.api.ModInitializer;

public class ServerLifecycleEventsTest implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.info("Server chunk loaded at {} {}", chunk.chunkZ, chunk.chunkZ);
			}
		});
		ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.info("Server chunk unloaded at {} {}", chunk.chunkZ, chunk.chunkZ);
			}
		});
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			LOGGER.info("Server Entity {} loaded", EntityType.getEntityName(entity));
		});
		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			LOGGER.info("Server Entity {} unloaded", EntityType.getEntityName(entity));
		});
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			LOGGER.info("Server starting");
		});
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LOGGER.info("Server started");
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			LOGGER.info("Server stopping");
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			LOGGER.info("Server stopped");
		});
	}
}
