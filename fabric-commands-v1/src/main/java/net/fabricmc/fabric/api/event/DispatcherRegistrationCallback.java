package net.fabricmc.fabric.api.event;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.commands.ServerCommandSource;

public interface DispatcherRegistrationCallback {

	Event<DispatcherRegistrationCallback> EVENT = EventFactory.createArrayBacked(
			DispatcherRegistrationCallback.class,
			(dispatcher, isDedicatedServer) -> {},
			callbacks -> (dispatcher, isDedicatedServer) -> {
				for (DispatcherRegistrationCallback callback : callbacks) {
					callback.initialize(dispatcher, isDedicatedServer);
				}
			});

	void initialize(CommandDispatcher<ServerCommandSource> dispatcher, boolean isDedicatedServer);
}
