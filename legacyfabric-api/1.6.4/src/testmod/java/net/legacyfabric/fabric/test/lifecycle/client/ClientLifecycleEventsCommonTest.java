/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.test.lifecycle.client;

import net.minecraft.entity.EntityType;

import net.fabricmc.api.ClientModInitializer;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class ClientLifecycleEventsCommonTest implements ClientModInitializer {
	private static final Logger LOGGER = Logger.get(LoggerImpl.API, "Test", "ClientLifecycleEvents");

	@Override
	public void onInitializeClient() {
		ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.trace("Client chunk loaded at %s %s", chunk.chunkX, chunk.chunkZ);
			}
		});
		ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			if (chunk != null) {
				LOGGER.trace("Client chunk unloaded at %s %s", chunk.chunkX, chunk.chunkZ);
			}
		});
		ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			LOGGER.trace("Client Entity %s loaded", EntityType.getEntityName(entity));
		});
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			LOGGER.trace("Client Entity %s unloaded", EntityType.getEntityName(entity));
		});
	}
}
