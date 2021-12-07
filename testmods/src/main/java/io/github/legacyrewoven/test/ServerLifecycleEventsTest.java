/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.legacyrewoven.test;

import io.github.legacyrewoven.api.event.lifecycle.v1.ServerChunkEvents;
import io.github.legacyrewoven.api.event.lifecycle.v1.ServerEntityEvents;
import io.github.legacyrewoven.api.event.lifecycle.v1.ServerLifecycleEvents;
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
				LOGGER.info("Server chunk loaded at {} {}", chunk.x, chunk.z);
			}
		});
		ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.info("Server chunk unloaded at {} {}", chunk.x, chunk.z);
			}
		});
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			LOGGER.info("Server Entity {} loaded", EntityType.getName(entity));
		});
		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			LOGGER.info("Server Entity {} unloaded", EntityType.getName(entity));
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
