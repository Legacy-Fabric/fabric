package net.fabricmc.fabric.api.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.options.GameOptions;

public interface GameOptionsSavedCallback {

	Event<GameOptionsSavedCallback> EVENT = EventFactory.createArrayBacked(GameOptionsSavedCallback.class,
			(listeners) -> (options) -> {
				for (GameOptionsSavedCallback event : listeners) {
					event.onGameOptionsSaved(options);
				}
			}
	);

	void onGameOptionsSaved(GameOptions options);

}
