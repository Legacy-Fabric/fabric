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

package net.legacyfabric.fabric.api.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.resource.loader.ResourcePackManagerImpl;

public class ResourcePackManager {
	public static InputStream openFile(String path) throws IOException {
		return ResourcePackManagerImpl.openFile(path);
	}

	/**
	 * @deprecated Use {@link #openFile(NamespacedIdentifier)} instead.
	 */
	@Deprecated
	public static InputStream openFile(Identifier id) throws IOException {
		return ResourcePackManagerImpl.openFile(id);
	}

	public static InputStream openFile(NamespacedIdentifier id) throws IOException {
		return ResourcePackManagerImpl.openFile(id);
	}

	/**
	 * @deprecated Use {@link #openAllFiles(NamespacedIdentifier)} instead.
	 */
	@Deprecated
	public static List<InputStream> openAllFiles(Identifier id) {
		return ResourcePackManagerImpl.openAllFiles(id);
	}

	public static List<InputStream> openAllFiles(NamespacedIdentifier id) {
		return ResourcePackManagerImpl.openAllFiles(id);
	}

	public static Set<String> getNamespaces() {
		return ResourcePackManagerImpl.getNamespaces();
	}

	public static ModResourcePack getModResourcePack(String modId) {
		return ResourcePackManagerImpl.getModResourcePack(modId);
	}

	public static List<ModResourcePack> getResourcePacks() {
		return ResourcePackManagerImpl.getResourcePacks();
	}

	/**
	 * @deprecated Use {@link #getAssetPath(NamespacedIdentifier)} instead.
	 */
	public static String getAssetPath(Identifier id) {
		return "/assets/" + id.getNamespace() + "/" + id.getPath();
	}

	public static String getAssetPath(NamespacedIdentifier id) {
		return "/assets/" + id.namespace() + "/" + id.identifier();
	}
}
