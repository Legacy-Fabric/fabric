/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.impl.content.registries;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.v1.FabricRegistryEntryAddedEvents;
import net.fabricmc.fabric.api.event.registry.v1.RegistryBlockAddedCallback;
import net.fabricmc.fabric.api.event.registry.v1.RegistryBlockEntityAddedCallback;
import net.fabricmc.fabric.api.event.registry.v1.RegistryEntityAddedCallback;
import net.fabricmc.fabric.api.event.registry.v1.RegistryItemAddedCallback;

public class LegacyEventInvokers implements ModInitializer {
	@Override
	public void onInitialize() {
		FabricRegistryEntryAddedEvents.ITEM.register(((id, item) -> RegistryItemAddedCallback.EVENT.invoker().itemAdded(id, item)));
		FabricRegistryEntryAddedEvents.BLOCK_ENTITY.register(((clazz, id) -> RegistryBlockEntityAddedCallback.EVENT.invoker().blockEntityAdded(clazz, id)));
		FabricRegistryEntryAddedEvents.ENTITY.register(((clazz, id) -> RegistryEntityAddedCallback.EVENT.invoker().entityAdded(clazz, id)));
		FabricRegistryEntryAddedEvents.BLOCK.register(((id, block) -> RegistryBlockAddedCallback.EVENT.invoker().blockAdded(id, block)));
	}
}
