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

package net.legacyfabric.fabric.impl.effect;

import net.minecraft.entity.effect.StatusEffect;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.event.RegistryInitializedEvent;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;
import net.legacyfabric.fabric.api.util.VersionUtils;

public class EarlyInitializer implements PreLaunchEntrypoint {
	/**
	 * Before MC 1.9, effects translation key starts with 'potion' instead of 'effect'.
	 */
	private static final String keyPrefix = VersionUtils.matches(">=1.9") ? "effect" : "potion";

	@Override
	public void onPreLaunch() {
		RegistryInitializedEvent.event(RegistryIds.STATUS_EFFECTS).register(EarlyInitializer::effectRegistryInit);
	}

	private static void effectRegistryInit(FabricRegistry<?> holder) {
		SyncedFabricRegistry<StatusEffect> registry = (SyncedFabricRegistry<StatusEffect>) holder;

		registry.fabric$getBeforeAddedCallback().register((rawId, id, object) -> object.setTranslationKey(keyPrefix + "." + id.toTranslationKey()));
	}
}
