package net.legacyfabric.fabric.test;

import net.legacyfabric.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.legacyfabric.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.legacyfabric.fabric.api.entity.event.v1.ServerPlayerEvents;
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
			LOGGER.info("{} went from dim {} to dim {}", EntityType.getEntityName(newEntity), origin.dimension.getName(), destination.dimension.getName());
		});
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
			LOGGER.info("Player went from dim {} to dim {}", origin.dimension.getName(), destination.dimension.getName());
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, world, alive) -> {
			LOGGER.info("Player {} respawned", newPlayer.getGameProfile().getName());
		});
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			LOGGER.info("{} player data is being copied", newPlayer.getGameProfile().getName());
		});
	}
}
