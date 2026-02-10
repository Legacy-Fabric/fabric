/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.test.entity;

import net.minecraft.entity.Entities;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.legacyfabric.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.legacyfabric.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class EntityEventsTest implements ModInitializer {
	private static final Logger LOGGER = Logger.get(LoggerImpl.API, "Test", "EntityEvents");

	@Override
	public void onInitialize() {
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((entity, killedEntity) -> {
			LOGGER.info("%s killed %s", Entities.getKey(entity), Entities.getKey(killedEntity));
		});
		ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
			LOGGER.info("%s went from dim %s to dim %s", Entities.getKey(newEntity), origin.dimension.getType().getName(), destination.dimension.getType().getName());
		});
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
			LOGGER.info("Player went from dim %s to dim %s", origin.dimension.getType().getName(), destination.dimension.getType().getName());
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, world, alive) -> {
			LOGGER.info("Player %s respawned", newPlayer.getGameProfile().getName());
		});
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			LOGGER.info("%s player data is being copied", newPlayer.getGameProfile().getName());
		});
	}
}
