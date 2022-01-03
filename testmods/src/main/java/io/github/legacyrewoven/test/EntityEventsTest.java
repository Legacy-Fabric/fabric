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

import io.github.legacyrewoven.api.entity.event.v1.ServerEntityCombatEvents;
import io.github.legacyrewoven.api.entity.event.v1.ServerEntityWorldChangeEvents;
import io.github.legacyrewoven.api.entity.event.v1.ServerPlayerEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityType;

import net.fabricmc.api.ModInitializer;

public class EntityEventsTest implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((entity, killedEntity) -> {
			LOGGER.info("{} killed {}", EntityType.getEntityName(entity), EntityType.getEntityName(killedEntity));
		});
		ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
			LOGGER.info("{} went from dim {} to dim {}", EntityType.getEntityName(newEntity), origin.dimension.method_11789().getName(), destination.dimension.method_11789().getName());
		});
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
			LOGGER.info("Player went from dim {} to dim {}", origin.dimension.method_11789().getName(), destination.dimension.method_11789().getName());
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, world, alive) -> {
			LOGGER.info("Player {} respawned", newPlayer.getGameProfile().getName());
		});
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			LOGGER.info("{} player data is being copied", newPlayer.getGameProfile().getName());
		});
	}
}
