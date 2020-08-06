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

package net.fabricmc.fabric.impl.event;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.*;
import net.fabricmc.fabric.api.event.world.ServerPlayerTickCallback;

public class LegacyEventInvokers implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerTickEvents.END_SERVER_TICK.register(server -> ServerTickCallback.EVENT.invoker().tick(server));
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerStopCallback.EVENT.invoker().onStopServer(server));
		ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerStartCallback.EVENT.invoker().onStartServer(server));
		ServerPlayerEvents.END_TICK.register(player -> ServerPlayerTickCallback.EVENT.invoker().tick(player));
		ServerPlayerEvents.CONNECT.register((connection, player) -> PlayerConnectCallback.EVENT.invoker().playerConnect(connection, player));
		ServerPlayerEvents.DISCONNECT.register(((connection, player, server) -> PlayerDisconnectCallback.EVENT.invoker().playerDisconnect(connection, player, server)));
	}
}