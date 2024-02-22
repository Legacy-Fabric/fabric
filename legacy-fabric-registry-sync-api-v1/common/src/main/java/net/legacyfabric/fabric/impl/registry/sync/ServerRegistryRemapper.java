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

package net.legacyfabric.fabric.impl.registry.sync;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapperRegistryRemapper;

public class ServerRegistryRemapper implements RegistryRemapperAccess {
	private static final RegistryRemapperAccess INSTANCE = new ServerRegistryRemapper();

	private final RegistryRemapper<RegistryRemapper<?>> REGISTRY_REMAPPER;

	@Override
	public RegistryRemapper<RegistryRemapper<?>> getRegistryRemapperRegistryRemapper() {
		return this.REGISTRY_REMAPPER;
	}

	@Override
	public void registrerRegistryRemapper(RegistryRemapper<?> remapper) {
		RegistryHelperImpl.registerRegistryRemapper(REGISTRY_REMAPPER, remapper);
		RegistryRemapper.REMAPPER_MAP.put(remapper.registryId, remapper);
		RegistryRemapper.REGISTRY_REMAPPER_MAP.put(remapper.getRegistry(), remapper);
	}

	public static RegistryRemapperAccess getInstance() {
		return INSTANCE;
	}

	private ServerRegistryRemapper() {
		REGISTRY_REMAPPER = new RegistryRemapperRegistryRemapper();

		RegistryRemapper.REMAPPER_MAP.put(REGISTRY_REMAPPER.registryId, REGISTRY_REMAPPER);
		RegistryRemapper.REGISTRY_REMAPPER_MAP.put(REGISTRY_REMAPPER.getRegistry(), REGISTRY_REMAPPER);
	}
}
