package net.fabricmc.fabric.api.event.server;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.MinecraftServer;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.command.CommandSide;

public interface FabricCommandRegisteredCallback {
	Event<FabricCommandRegisteredCallback> EVENT = EventFactory.createArrayBacked(FabricCommandRegisteredCallback.class,
			(listeners) -> (server, command, side) -> {
				for (FabricCommandRegisteredCallback event : listeners) {
					event.onCommandRegistered(server, command, side);
				}
			}
	);

	void onCommandRegistered(MinecraftServer server, AbstractCommand command, CommandSide side);
}
