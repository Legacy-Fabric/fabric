package net.fabricmc.fabric.api.event.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface ServerPlayerTickCallback {
	Event<ServerPlayerTickCallback> EVENT = EventFactory.createArrayBacked(ServerPlayerTickCallback.class,
			(listeners) -> (player) -> {
				for (ServerPlayerTickCallback event : listeners) {
					event.tick(player);
				}
			}
	);

	void tick(PlayerEntity player);
}
