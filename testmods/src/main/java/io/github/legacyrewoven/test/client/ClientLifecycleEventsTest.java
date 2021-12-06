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

package io.github.legacyrewoven.test.client;

import io.github.legacyrewoven.api.client.event.lifecycle.v1.ClientChunkEvents;
import io.github.legacyrewoven.api.client.event.lifecycle.v1.ClientEntityEvents;
import io.github.legacyrewoven.api.client.event.lifecycle.v1.ClientLifecycleEvents;
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
				LOGGER.info("Client chunk loaded at {} {}", chunk.x, chunk.z);
			}
		});
		ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.info("Client chunk unloaded at {} {}", chunk.x, chunk.z);
			}
		});
		ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			LOGGER.info("Client Entity {} loaded", EntityType.getName(entity));
		});
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			LOGGER.info("Client Entity {} unloaded", EntityType.getName(entity));
		});
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			LOGGER.info("Client started");
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			LOGGER.info("Client stopping");
		});
	}
}
