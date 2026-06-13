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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftInstance;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resource.manager.ReloadableResourceManager;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener;
import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloader;
import net.legacyfabric.fabric.api.resource.ResourceManagerHelper;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class ResourceManagerHelperImpl implements ResourceManagerHelper, ClientModInitializer {
	private static final ResourceManagerHelperImpl INSTANCE = new ResourceManagerHelperImpl();
	public static final Logger LOGGER = Logger.get(LoggerImpl.API, "ResourceManagerHelperImpl");

	private static final Set<NamespacedIdentifier> addedListenerIds = new HashSet<>();
	private static final Set<IdentifiableResourceReloadListener> addedListeners = new LinkedHashSet<>();
	private final Map<String, IdentifiableResourceReloadListener> wrappedListener = new HashMap<>();
	private static boolean initialized = false;

	public static ResourceManagerHelperImpl getInstance() {
		return INSTANCE;
	}

	public void sort(List<ResourceReloader> listeners) {
		listeners.sort(Comparator.comparing(
				this::getReloaderInfos,
				(firstReloader, secondReloader) -> {
					if (firstReloader != null && secondReloader != null) {
						if (secondReloader.getRight().contains(firstReloader.getLeft())) {
							return -1;
						} else if (firstReloader.getRight().contains(secondReloader.getLeft())) {
							return 1;
						}
					}

					return 0;
				}
		));

		Set<NamespacedIdentifier> resolvedIds = new HashSet<>();

		for (ResourceReloader listener : listeners) {
			Pair<? extends NamespacedIdentifier, ? extends Collection<? extends NamespacedIdentifier>> infos = getReloaderInfos(listener);

			if (infos != null) {
				resolvedIds.add(infos.getLeft());
			}
		}

		for (ResourceReloader reloader: listeners) {
			Pair<? extends NamespacedIdentifier, ? extends Collection<? extends NamespacedIdentifier>> infos = getReloaderInfos(reloader);

			if (infos != null) {
				Set<NamespacedIdentifier> missing = new HashSet<>(infos.getRight());
				missing.removeIf(resolvedIds::contains);

				if (!missing.isEmpty()) {
					LOGGER.warn("Could not resolve dependencies for listener: " + infos.getLeft() + "! Missing: " + missing);
				}
			}
		}
	}

	private @Nullable Pair<? extends NamespacedIdentifier, ? extends Collection<? extends NamespacedIdentifier>> getReloaderInfos(ResourceReloader reloader) {
		if (reloader instanceof IdentifiableResourceReloader) {
			return Pair.of(((IdentifiableResourceReloader) reloader).getFabricId(), ((IdentifiableResourceReloader) reloader).getFabricDependencies());
		} else if (reloader.getClass().getName().equals("net.minecraft.resource.ReloadListener")) {
			if (wrappedListener.containsKey(reloader.getClass().getName())) {
				IdentifiableResourceReloadListener resourceReloadListener = wrappedListener.get(reloader.getClass().getName());
				return Pair.of(resourceReloadListener.getFabricId(), resourceReloadListener.getFabricDependencies());
			}
		}

		return null;
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

	public void registerWrappedListener(IdentifiableResourceReloadListener listener) {
		wrappedListener.put(listener.getClass().getSimpleName(), listener);
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
}
