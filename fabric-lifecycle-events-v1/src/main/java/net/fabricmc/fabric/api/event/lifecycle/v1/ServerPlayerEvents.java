/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ServerPlayerEvents {
	private ServerPlayerEvents() {
	}

	/**
	 * Called when a Player connects to a server.
	 */
	public static final Event<Connect> CONNECT = EventFactory.createArrayBacked(Connect.class, (listeners) -> (conn, player) -> {
		for (Connect callback : listeners) {
			callback.playerConnect(conn, player);
		}
	});

	/**
	 * Called when a Player disconnects from a server.
	 */
	public static final Event<Disconnect> DISCONNECT = EventFactory.createArrayBacked(Disconnect.class, (listeners) -> (conn, player, server) -> {
		for (Disconnect callback : listeners) {
			callback.playerDisconnect(conn, player, server);
		}
	});

	/**
	 * Called at the start of {@link ServerPlayerEntity#tick()}.
	 */
	public static final Event<StartTick> START_TICK = EventFactory.createArrayBacked(StartTick.class,
			(listeners) -> (player) -> {
				for (StartTick event : listeners) {
					event.tick(player);
				}
			}
	);

	/**
	 * Called at the end of {@link ServerPlayerEntity#tick()}.
	 */
	public static final Event<EndTick> END_TICK = EventFactory.createArrayBacked(EndTick.class,
			(listeners) -> (player) -> {
				for (EndTick event : listeners) {
					event.tick(player);
				}
			}
	);

	public interface Connect {
		void playerConnect(ClientConnection connection, ServerPlayerEntity player);
	}

	public interface Disconnect {
		void playerDisconnect(ClientConnection connection, ServerPlayerEntity player, MinecraftServer server);
	}

	public interface StartTick {
		void tick(ServerPlayerEntity player);
	}

	public interface EndTick {
		void tick(ServerPlayerEntity player);
	}
}
