package net.fabricmc.fabric.api.event.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ChunksSavedCallback {
	Event<ChunksSavedCallback> EVENT = EventFactory.createArrayBacked(ChunksSavedCallback.class, (listeners) -> () -> {
		for (ChunksSavedCallback callback : listeners) {
			callback.chunksSaved();
		}
	});

	void chunksSaved();
}
