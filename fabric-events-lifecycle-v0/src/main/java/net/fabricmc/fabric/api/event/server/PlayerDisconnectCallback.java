package net.fabricmc.fabric.api.event.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;

public interface PlayerDisconnectCallback {

	Event<PlayerDisconnectCallback> EVENT = EventFactory.createArrayBacked(PlayerDisconnectCallback.class, (listeners) -> (conn, player,server) -> {
		for (PlayerDisconnectCallback callback : listeners) {
			callback.playerDisconnect(conn,player,server);
		}
	});

	void playerDisconnect(ClientConnection connection, ServerPlayerEntity player, MinecraftServer server);
}
