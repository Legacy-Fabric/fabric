package net.fabricmc.fabric.api.event.registry.v1;

import net.minecraft.entity.Entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RegistryEntityAddedCallback {
	Event<RegistryEntityAddedCallback> EVENT = EventFactory.createArrayBacked(RegistryEntityAddedCallback.class, (listeners) -> (name, entityClass) -> {
		for (RegistryEntityAddedCallback callback : listeners) {
			callback.entityAdded(name, entityClass);
		}
	});

	void entityAdded(Class<? extends Entity> entityClass, String name);
}
