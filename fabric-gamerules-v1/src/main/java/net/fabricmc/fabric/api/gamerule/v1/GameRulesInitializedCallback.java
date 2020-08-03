package net.fabricmc.fabric.api.gamerule.v1;

import net.minecraft.world.GameRules;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GameRulesInitializedCallback {
	Event<GameRulesInitializedCallback> EVENT = EventFactory.createArrayBacked(GameRulesInitializedCallback.class,
			(listeners) -> (dispatcher) -> {
				for (GameRulesInitializedCallback event : listeners) {
					event.onGamerulesRegistered(dispatcher);
				}
			}
	);

	void onGamerulesRegistered(GameRules dispatcher);
}
