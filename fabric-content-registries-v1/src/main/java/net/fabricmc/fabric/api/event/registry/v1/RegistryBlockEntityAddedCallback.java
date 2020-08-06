package net.fabricmc.fabric.api.event.registry.v1;

import net.minecraft.block.entity.BlockEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RegistryBlockEntityAddedCallback {
	Event<RegistryBlockEntityAddedCallback> EVENT = EventFactory.createArrayBacked(RegistryBlockEntityAddedCallback.class, (listeners) -> (blockEntityClass, name) -> {
		for (RegistryBlockEntityAddedCallback callback : listeners) {
			callback.blockEntityAdded(blockEntityClass, name);
		}
	});

	void blockEntityAdded(Class<? extends BlockEntity> blockEntityClass, String name);
}
