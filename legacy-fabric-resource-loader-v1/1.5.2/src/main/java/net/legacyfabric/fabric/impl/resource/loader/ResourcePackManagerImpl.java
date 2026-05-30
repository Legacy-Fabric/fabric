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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.resource.ModResourcePack;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class ResourcePackManagerImpl {
	public static final Logger LOGGER = Logger.get(LoggerImpl.API, "ResourcePackManagerImpl");

	public static InputStream openFile(String fileName) throws IOException {
		return ResourceManager.client().getResource(fileName);
	}

	public static InputStream openFile(NamespacedIdentifier fileName) throws IOException {
		Optional<Resource> resource = ResourceManager.client().getResource(fileName);

		if (resource.isPresent()) {
			return resource.get().open();
		}

		return null;
	}

	public static List<InputStream> openAllFiles(NamespacedIdentifier fileName) {
		List<Resource> resources = ResourceManager.client().getResourceStack(fileName);

		List<InputStream> list = new ArrayList<>();

		for (Resource resource : resources) {
			try {
				InputStream open = resource.open();
				list.add(open);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public static Set<String> getNamespaces() {
		return ResourceManager.client().getNamespaces();
	}

	public static ModResourcePack getModResourcePack(String modId) {
		return null;
	}

	public static List<ModResourcePack> getResourcePacks() {
		return Collections.emptyList();
	}
}
