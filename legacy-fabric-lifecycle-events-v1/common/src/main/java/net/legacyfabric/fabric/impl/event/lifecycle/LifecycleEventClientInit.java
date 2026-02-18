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

import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class LifecycleEventClientInit implements ClientModInitializer {
	@Override
	public void initClient() {
		MinecraftClientEvents.READY.register(ClientLifecycleEvents.CLIENT_STARTED.invoker()::onClientStarted);
		MinecraftClientEvents.STOP.register(ClientLifecycleEvents.CLIENT_STOPPING.invoker()::onClientStopping);
		MinecraftClientEvents.TICK_START.register(ClientTickEvents.START_CLIENT_TICK.invoker()::onStartTick);
		MinecraftClientEvents.TICK_END.register(ClientTickEvents.END_CLIENT_TICK.invoker()::onEndTick);
		ClientWorldEvents.TICK_END.register(ClientTickEvents.END_WORLD_TICK.invoker()::onEndTick);
	}
}
