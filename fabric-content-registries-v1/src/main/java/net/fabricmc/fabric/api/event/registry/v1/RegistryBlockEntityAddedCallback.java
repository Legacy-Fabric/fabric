/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.fabricmc.fabric.api.event.registry.v1;

import net.minecraft.block.entity.BlockEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Deprecated
public interface RegistryBlockEntityAddedCallback {
	/**
	 * Please use {@link FabricRegistryEntryAddedEvents#BLOCK_ENTITY} instead!
	 */
	@Deprecated
	Event<RegistryBlockEntityAddedCallback> EVENT = EventFactory.createArrayBacked(RegistryBlockEntityAddedCallback.class, (listeners) -> (blockEntityClass, name) -> {
		for (RegistryBlockEntityAddedCallback callback : listeners) {
			callback.blockEntityAdded(blockEntityClass, name);
		}
	});

	void blockEntityAdded(Class<? extends BlockEntity> blockEntityClass, String name);
}
