package net.fabricmc.fabric.api.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.level.LevelInfo;

public interface LanServerPublishedCallback {
	Event<LanServerPublishedCallback> EVENT = EventFactory.createArrayBacked(LanServerPublishedCallback.class,
			(listeners) -> (client,gameMode,cheats,levelinfo) -> {
				for (LanServerPublishedCallback event : listeners) {
					event.onServerPublished(client,gameMode,cheats,levelinfo);
				}
			}
	);

	void onServerPublished(MinecraftClient client,LevelInfo.GameMode gameMode, boolean cheats,LevelInfo levelInfo);
}
