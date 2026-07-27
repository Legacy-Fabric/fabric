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

package net.legacyfabric.fabric.impl.item;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

import net.minecraft.item.Item;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.items.api.ItemEvents;
import net.ornithemc.osl.items.api.ItemRegistry;

public class EarlyInitializer implements ModInitializer {
	@Override
	public void init() {
		ItemEvents.REGISTER_ITEMS.register(() -> {
			RegistryHelperImplementation.registerCompatId(RegistryIds.ITEMS, ItemRegistry.REGISTRY);
			RegistryHelperImplementation.registerCompatRegistry((FabricRegistry<Item>) Item.REGISTRY, ItemRegistry.REGISTRY);
		});
	}
}
