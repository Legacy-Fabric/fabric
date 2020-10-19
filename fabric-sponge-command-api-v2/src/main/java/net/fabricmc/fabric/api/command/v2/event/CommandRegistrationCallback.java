package net.fabricmc.fabric.api.command.v2.event;

import net.minecraft.server.MinecraftServer;

import net.fabricmc.fabric.api.command.v2.lib.sponge.dispatcher.Dispatcher;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface CommandRegistrationCallback {
	Event<CommandRegistrationCallback> EVENT = EventFactory.createArrayBacked(CommandRegistrationCallback.class, listeners -> (dispatcher, server) -> {
		for (CommandRegistrationCallback callback : listeners) {
			callback.register(dispatcher, server);
		}
	});

	void register(Dispatcher dispatcher, MinecraftServer server);
}
