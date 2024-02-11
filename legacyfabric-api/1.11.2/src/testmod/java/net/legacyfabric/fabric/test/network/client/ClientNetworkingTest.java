/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.test.network.client;

import java.util.Arrays;

import net.fabricmc.api.ClientModInitializer;

import net.legacyfabric.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.impl.logger.LoggerHelper;

public class ClientNetworkingTest implements ClientModInitializer {
	private static final Logger LOGGER = Logger.get(LoggerHelper.API, "Test", "ClientNetworking");

	@Override
	public void onInitializeClient() {
		C2SPlayChannelEvents.REGISTER.register((handler, sender, client, channels) -> {
			LOGGER.info("Registered channels (C2S callback) - " + Arrays.toString(channels.toArray()));
		});
		C2SPlayChannelEvents.UNREGISTER.register((handler, sender, client, channels) -> {
			LOGGER.info("Unregistered channels (C2S callback) - " + Arrays.toString(channels.toArray()));
		});
		ClientPlayConnectionEvents.INIT.register((handler, client) -> {
			LOGGER.info("Connection initialized (C2S)");
		});
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			LOGGER.info("World joined (C2S)");
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			LOGGER.info("Connection disconnected (C2S)");
		});
	}
}
