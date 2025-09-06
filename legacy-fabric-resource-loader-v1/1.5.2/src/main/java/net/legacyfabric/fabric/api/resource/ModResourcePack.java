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

package net.legacyfabric.fabric.api.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import net.legacyfabric.fabric.api.util.Identifier;

/**
 * Interface implemented by mod-provided resource packs.
 */
public interface ModResourcePack {
	/**
	 * @return The ModMetadata object associated with the mod providing this
	 * resource pack.
	 */
	ModMetadata getFabricModMetadata();

	/**
	 * @return The owner ModContainer of this resource pack.
	 */
	ModContainer getOwner();

	InputStream openFile(String filename) throws IOException;
	boolean containsFile(String filename);
	InputStream openFile(Identifier id) throws IOException;
	boolean containsFile(Identifier id);
	Set<String> getNamespaces();
	String getName();
}
