/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.legacyfabric.fabric.test.network;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.legacyfabric.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ServerNetworkingTest implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		S2CPlayChannelEvents.REGISTER.register((handler, sender, server, channels) -> {
			LOGGER.info("Registered channels (S2C callback) - " + Arrays.toString(channels.toArray()));
		});
		S2CPlayChannelEvents.UNREGISTER.register((handler, sender, server, channels) -> {
			LOGGER.info("Unregistered channels (S2C callback) - " + Arrays.toString(channels.toArray()));
		});
		ServerPlayConnectionEvents.INIT.register((handler, server) -> {
			LOGGER.info("Connection initialized (S2C)");
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			LOGGER.info("World joined (S2C)");
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			LOGGER.info("Connection disconnected (S2C)");
		});
	}
}
