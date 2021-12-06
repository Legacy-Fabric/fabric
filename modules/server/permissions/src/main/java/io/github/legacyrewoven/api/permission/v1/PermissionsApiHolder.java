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

package io.github.legacyrewoven.api.permission.v1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionsApiHolder {
	private static final Logger LOGGER = LogManager.getLogger();
	private static PlayerPermissionsApi PLAYER_PERMISSIONS_API = null;

	public static boolean setPlayerPermissionsApi(PlayerPermissionsApi api) {
		if (PLAYER_PERMISSIONS_API == null) {
			PLAYER_PERMISSIONS_API = api;
			return true;
		}

		LOGGER.error("Cannot register player permissions api with id {}. There is already a permissions api implementor!", api.getId());
		return false;
	}

	public static PlayerPermissionsApi getPlayerPermissionsApi() {
		return PLAYER_PERMISSIONS_API != null ? PLAYER_PERMISSIONS_API : FallbackPlayerPermissionsApi.INSTANCE;
	}

	private static class FallbackPlayerPermissionsApi implements PlayerPermissionsApi {
		private static final FallbackPlayerPermissionsApi INSTANCE = new FallbackPlayerPermissionsApi();

		private FallbackPlayerPermissionsApi() {
		}

		@Override
		public String getId() {
			return "legacy-fabric-fallback-permissions-api";
		}

		@Override
		public boolean hasPermission(ServerPlayerEntity player, String perm) {
			return MinecraftClient.getInstance().getServer().getPlayerManager().isOperator(player.getGameProfile());
		}
	}
}
