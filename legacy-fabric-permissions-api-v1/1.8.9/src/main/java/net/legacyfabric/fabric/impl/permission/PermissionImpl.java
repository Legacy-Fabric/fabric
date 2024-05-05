/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.impl.permission;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import net.legacyfabric.fabric.api.permission.v1.PermissionsApiHolder;
import net.legacyfabric.fabric.api.permission.v1.PlayerPermissionsApi;

public class PermissionImpl implements ModInitializer {
	@Override
	public void onInitialize() {
		PermissionsApiHolder.setFallbackPlayerPermissionsApi(FallbackPlayerPermissionsApi.INSTANCE);
	}

	private enum FallbackPlayerPermissionsApi implements PlayerPermissionsApi {
		INSTANCE;

		@Override
		public String getId() {
			return "legacy-fabric-fallback-permissions-api";
		}

		@Override
		public boolean hasPermission(ServerPlayerEntity player, String perm) {
			return getServer().getPlayerManager().isOperator(player.getGameProfile());
		}
	}

	protected static MinecraftServer getServer() {
		try {
			return MinecraftServer.getServer();
		} catch (NoSuchMethodError e) {
			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				return MinecraftClient.getInstance().getServer();
			} else {
				return (MinecraftServer) FabricLoader.getInstance().getGameInstance();
			}
		}
	}
}
