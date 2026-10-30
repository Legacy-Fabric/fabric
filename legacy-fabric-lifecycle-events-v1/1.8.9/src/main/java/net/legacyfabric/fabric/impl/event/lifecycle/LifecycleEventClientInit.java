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

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.ClientWorldEvents;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class LifecycleEventClientInit implements ClientModInitializer {
	@Override
	public void initClient() {
		MinecraftClientEvents.READY.register(LifecycleEventClientInit::clientStarted);
		MinecraftClientEvents.STOP.register(LifecycleEventClientInit::clientStopping);
		MinecraftClientEvents.TICK_START.register(LifecycleEventClientInit::clientTickStart);
		MinecraftClientEvents.TICK_END.register(LifecycleEventClientInit::clientTickEnd);
		ClientWorldEvents.TICK_END.register(LifecycleEventClientInit::clientWorldTickEnd);
	}

	private static void clientStarted(Minecraft client) {
		ClientLifecycleEvents.CLIENT_STARTED.invoker().onClientStarted(client);
	}

	private static void clientStopping(Minecraft client) {
		ClientLifecycleEvents.CLIENT_STOPPING.invoker().onClientStopping(client);
	}

	private static void clientTickStart(Minecraft client) {
		ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick(client);
	}

	private static void clientTickEnd(Minecraft client) {
		ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick(client);
	}

	private static void clientWorldTickEnd(ClientWorld world) {
		ClientTickEvents.END_WORLD_TICK.invoker().onEndTick(world);
	}
}
