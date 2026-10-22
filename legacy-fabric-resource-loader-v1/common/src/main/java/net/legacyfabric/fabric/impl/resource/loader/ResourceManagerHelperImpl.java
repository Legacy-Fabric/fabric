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

package net.legacyfabric.fabric.impl.resource.loader;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftInstance;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.client.resource.reload.AssetsReloaders;

import net.minecraft.client.resource.manager.ReloadableResourceManager;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener;
import net.legacyfabric.fabric.api.resource.ResourceManagerHelper;
import net.legacyfabric.fabric.api.resource.ResourceReloadListenerKeys;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class ResourceManagerHelperImpl implements ResourceManagerHelper, ClientModInitializer {
	private static final ResourceManagerHelperImpl INSTANCE = new ResourceManagerHelperImpl();
	public static final Logger LOGGER = Logger.get(LoggerImpl.API, "ResourceManagerHelperImpl");

	private static final Set<NamespacedIdentifier> addedListenerIds = new HashSet<>();
	private static final Set<IdentifiableResourceReloadListener> addedListeners = new LinkedHashSet<>();
	private static boolean initialized = false;

	public static ResourceManagerHelperImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public void registerReloadListener(IdentifiableResourceReloadListener listener) {
		if (initialized) {
			throw new RuntimeException("Tried to register resource reload listener " + listener.getFabricId() + " after initialization!");
		}

		if (!addedListenerIds.add(listener.getFabricId())) {
			LOGGER.warn("Tried to register resource reload listener " + listener.getFabricId() + " twice!");
			return;
		}

		if (!addedListeners.add(listener)) {
			throw new RuntimeException("Listener with previously unknown ID " + listener.getFabricId() + " already in listener set!");
		}
	}

	@Override
	public void initClient() {
		ClientResourceLoaderEvents.INIT_RESOURCE_MANAGER.register(reloadableResourceManager -> {
			for (IdentifiableResourceReloadListener listener : addedListeners) {
				((ReloadableResourceManager) MinecraftInstance.get().getResourceManager()).addListener(listener);
			}

			initialized = true;
		});
	}

	public static NamespacedIdentifier mapDependencyId(NamespacedIdentifier oldId) {
		if (oldId == ResourceReloadListenerKeys.SOUNDS) {
			return AssetsReloaders.SOUND_MANAGER;
		}

		if (oldId == ResourceReloadListenerKeys.FONTS) {
			return AssetsReloaders.FONT_MANAGER;
		}

		if (oldId == ResourceReloadListenerKeys.MODELS) {
			return AssetsReloaders.MODEL_MANAGER;
		}

		if (oldId == ResourceReloadListenerKeys.LANGUAGES) {
			return AssetsReloaders.LANGUAGE_MANAGER;
		}

		if (oldId == ResourceReloadListenerKeys.TEXTURES) {
			return AssetsReloaders.TEXTURE_MANAGER;
		}

		return oldId;
	}
}
