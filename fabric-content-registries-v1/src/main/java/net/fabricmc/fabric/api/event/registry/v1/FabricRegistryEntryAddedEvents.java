/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class FabricRegistryEntryAddedEvents {
	public static final Event<RegistryBlockAddedCallback> BLOCK = EventFactory.createArrayBacked(RegistryBlockAddedCallback.class, (listeners) -> (id, block) -> {
		for (RegistryBlockAddedCallback callback : listeners) {
			callback.blockAdded(id, block);
		}
	});

	public static final Event<RegistryBlockEntityAddedCallback> BLOCK_ENTITY = EventFactory.createArrayBacked(RegistryBlockEntityAddedCallback.class, (listeners) -> (blockEntityClass, name) -> {
		for (RegistryBlockEntityAddedCallback callback : listeners) {
			callback.blockEntityAdded(blockEntityClass, name);
		}
	});

	public static final Event<RegistryEntityAddedCallback> ENTITY = EventFactory.createArrayBacked(RegistryEntityAddedCallback.class, (listeners) -> (name, entityClass) -> {
		for (RegistryEntityAddedCallback callback : listeners) {
			callback.entityAdded(name, entityClass);
		}
	});

	public static final Event<RegistryItemAddedCallback> ITEM = EventFactory.createArrayBacked(RegistryItemAddedCallback.class, (listeners) -> (id, item) -> {
		for (RegistryItemAddedCallback callback : listeners) {
			callback.itemAdded(id, item);
		}
	});

	@FunctionalInterface
	public interface RegistryBlockAddedCallback {
		void blockAdded(Identifier id, Block block);
	}

	@FunctionalInterface
	public interface RegistryBlockEntityAddedCallback {
		void blockEntityAdded(Class<? extends BlockEntity> blockEntityClass, String name);
	}

	@FunctionalInterface
	public interface RegistryEntityAddedCallback {
		void entityAdded(Class<? extends Entity> entityClass, String name);
	}

	@FunctionalInterface
	public interface RegistryItemAddedCallback {
		void itemAdded(Identifier id, Item item);
	}
}
