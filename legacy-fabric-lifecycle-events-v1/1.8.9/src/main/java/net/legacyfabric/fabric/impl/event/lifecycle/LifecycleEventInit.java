/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.impl.event.lifecycle;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.lifecycle.api.server.ServerWorldEvents;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class LifecycleEventInit implements ModInitializer {
	@Override
	public void init() {
		MinecraftServerEvents.START.register(LifecycleEventInit::serverStarting);
		MinecraftServerEvents.STOP.register(LifecycleEventInit::serverStopping);
		MinecraftServerEvents.READY_WORLD.register(LifecycleEventInit::loadWorldsEvent);
		ServerWorldEvents.TICK_END.register(LifecycleEventInit::worldTickEnd);
	}

	private static void serverStarting(MinecraftServer server) {
		ServerLifecycleEvents.SERVER_STARTING.invoker().onServerStarting(server);
	}

	private static void serverStopping(MinecraftServer server) {
		ServerLifecycleEvents.SERVER_STOPPING.invoker().onServerStopping(server);
	}

	private static void worldTickEnd(ServerWorld world) {
		ServerTickEvents.END_WORLD_TICK.invoker().onEndTick(world);
	}

	private static void loadWorldsEvent(MinecraftServer server) {
		for (ServerWorld world : server.worlds) {
			net.legacyfabric.fabric.api.event.lifecycle.v1.ServerWorldEvents.LOAD.invoker().onWorldLoad(server, world);
		}
	}
}
