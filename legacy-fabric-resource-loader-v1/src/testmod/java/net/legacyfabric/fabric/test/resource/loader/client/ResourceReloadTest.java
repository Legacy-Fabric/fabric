/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

package net.legacyfabric.fabric.test.resource.loader.client;

import java.util.Arrays;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ClientModInitializer;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener;
import net.legacyfabric.fabric.api.resource.ResourceManagerHelper;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

public class ResourceReloadTest implements ClientModInitializer {
	private static final Logger LOGGER = Logger.get(LoggerImpl.API, "Test", "ResourceReload");

	@Override
	public void onInitializeClient() {
		Identifier id = new Identifier("legacy-fabric-api", "test_reload");
		ResourceManagerHelper.getInstance().registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return id;
			}

			@Override
			public void reload(ResourceManager resourceManager) {
				LOGGER.info("Resources and reloading");
				LOGGER.info("Namespaces are %s", Arrays.toString(resourceManager.getAllNamespaces().toArray()));
			}
		});
	}
}
