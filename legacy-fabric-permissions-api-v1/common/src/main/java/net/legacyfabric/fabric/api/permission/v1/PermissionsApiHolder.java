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

package net.legacyfabric.fabric.api.permission.v1;

import org.jetbrains.annotations.ApiStatus;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

/**
 * @deprecated Unstable API, may change in the future.
 */
@Deprecated
@ApiStatus.Experimental
public class PermissionsApiHolder {
	private static final Logger LOGGER = Logger.get(LoggerImpl.API, "PermissionApiHolder");
	private static PlayerPermissionsApi PLAYER_PERMISSIONS_API = null;
	private static PlayerPermissionsApi FALLBACK_PLAYER_PERMISSIONS_API = null;

	public static boolean setPlayerPermissionsApi(PlayerPermissionsApi api) {
		if (PLAYER_PERMISSIONS_API == null) {
			PLAYER_PERMISSIONS_API = api;
			return true;
		}

		LOGGER.error("Cannot register player permissions api with id %s. There is already a permissions api implementor!", api.getId());
		return false;
	}

	public static PlayerPermissionsApi getPlayerPermissionsApi() {
		return PLAYER_PERMISSIONS_API != null ? PLAYER_PERMISSIONS_API : FALLBACK_PLAYER_PERMISSIONS_API;
	}

	@ApiStatus.Internal
	public static void setFallbackPlayerPermissionsApi(PlayerPermissionsApi api) {
		FALLBACK_PLAYER_PERMISSIONS_API = api;
	}
}
