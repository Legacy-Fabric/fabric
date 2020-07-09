package net.fabricmc.fabric.api.event.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;

public interface EntityKilledCallback {
	Event<EntityKilledCallback> EVENT = EventFactory.createArrayBacked(EntityKilledCallback.class, (listeners) -> (entity) -> {
		for (EntityKilledCallback callback : listeners) {
			callback.entityKilled(entity);
		}
	});

	void entityKilled(Entity killed);
}
