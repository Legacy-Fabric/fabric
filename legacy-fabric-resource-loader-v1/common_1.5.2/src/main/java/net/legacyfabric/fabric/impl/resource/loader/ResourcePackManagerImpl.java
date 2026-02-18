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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.resource.ModResourcePack;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class ResourcePackManagerImpl {
	public static final Logger LOGGER = Logger.get(LoggerImpl.API, "ResourcePackManagerImpl");
	private static final List<ModResourcePack> RESOURCE_PACKS = new ArrayList<>();
	private static boolean initialized = false;

	private static void init() {
		if (initialized) {
			return;
		}

		ModResourcePackUtil.appendModResourcePacks(RESOURCE_PACKS);

		initialized = true;
	}

	public static InputStream openFile(String fileName) throws IOException {
		init();

		for (ModResourcePack pack : RESOURCE_PACKS) {
			if (pack.containsFile(fileName)) return pack.openFile(fileName);
		}

		return null;
	}

	public static InputStream openFile(NamespacedIdentifier fileName) throws IOException {
		init();

		for (ModResourcePack pack : RESOURCE_PACKS) {
			if (pack.containsFile(fileName)) return pack.openFile(fileName);
		}

		return null;
	}

	public static List<InputStream> openAllFiles(NamespacedIdentifier fileName) {
		init();

		List<InputStream> streams = new ArrayList<>();

		for (ModResourcePack pack : RESOURCE_PACKS) {
			if (pack.containsFile(fileName)) {
				try {
					streams.add(pack.openFile(fileName));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return streams;
	}

	public static Set<String> getNamespaces() {
		init();

		Set<String> namespaces = new HashSet<>();

		for (ModResourcePack pack : RESOURCE_PACKS) {
			namespaces.addAll(pack.getNamespaces());
		}

		return namespaces;
	}

	public static ModResourcePack getModResourcePack(String modId) {
		init();

		for (ModResourcePack pack : RESOURCE_PACKS) {
			if (pack.getFabricModMetadata().getId().equals(modId)) {
				return pack;
			}
		}

		return null;
	}

	public static List<ModResourcePack> getResourcePacks() {
		init();

		return RESOURCE_PACKS;
	}
}
