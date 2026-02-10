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

package net.legacyfabric.fabric.impl.resource.loader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import net.legacyfabric.fabric.api.resource.ModResourcePack;

/**
 * Internal utilities for managing resource packs.
 */
public final class ModResourcePackUtil {
	public static final int PACK_FORMAT_VERSION = 1;

	private ModResourcePackUtil() {
	}

	public static void appendModResourcePacks(List<ModResourcePack> packList) {
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			if (container.getMetadata().getType().equals("builtin")) {
				continue;
			}

			for (Path path : container.getRootPaths()) {
				ModResourcePack pack = new ModNioResourcePack(container, path, null);

				if (!pack.getNamespaces().isEmpty()) {
					packList.add(pack);
				}
			}
		}
	}

	public static boolean containsDefault(ModMetadata info, String filename) {
		return "/pack.txt".equals(filename);
	}

	public static InputStream openDefault(ModMetadata info, String filename) {
		if ("/pack.txt".equals(filename)) {
			String description = info.getName();

			return new ByteArrayInputStream(description.getBytes(StandardCharsets.UTF_8));
		}

		return null;
	}

	public static String getName(ModMetadata info) {
		if (info.getName() != null) {
			return info.getName();
		} else {
			return "Fabric Mod \"" + info.getId() + "\"";
		}
	}
}
