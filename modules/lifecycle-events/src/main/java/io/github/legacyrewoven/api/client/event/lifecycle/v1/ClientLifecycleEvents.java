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

package io.github.legacyrewoven.api.client.event.lifecycle.v1;

import io.github.legacyrewoven.api.event.Event;
import io.github.legacyrewoven.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ClientLifecycleEvents {
	private ClientLifecycleEvents() {
	}

	/**
	 * Called when Minecraft has started and it's client about to tick for the first time.
	 */
	public static final Event<ClientStarted> CLIENT_STARTED = EventFactory.createArrayBacked(ClientStarted.class, callbacks -> client -> {
		for (ClientStarted callback : callbacks) {
			callback.onClientStarted(client);
		}
	});

	/**
	 * Called when Minecraft's client begins to stop.
	 * This is caused by quitting while in game, or closing the game window.
	 */
	public static final Event<ClientStopping> CLIENT_STOPPING = EventFactory.createArrayBacked(ClientStopping.class, callbacks -> client -> {
		for (ClientStopping callback : callbacks) {
			callback.onClientStopping(client);
		}
	});

	@FunctionalInterface
	public interface ClientStarted {
		void onClientStarted(MinecraftClient client);
	}

	@FunctionalInterface
	public interface ClientStopping {
		void onClientStopping(MinecraftClient client);
	}

	@FunctionalInterface
	public interface OutOfMemory {
		void onOutOfMemoryError(MinecraftClient client);
	}

	@FunctionalInterface
	public interface IntegratedServerPublished {
		void onServerPublished(MinecraftClient client, GameMode gameMode, boolean cheats, LevelInfo levelInfo);
	}

	@FunctionalInterface
	public interface OptionsSaved {
		void onGameOptionsSaved(GameOptions options);
	}
}
