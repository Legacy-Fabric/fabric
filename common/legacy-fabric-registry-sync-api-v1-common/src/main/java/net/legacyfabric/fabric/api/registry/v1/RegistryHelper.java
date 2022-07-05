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

package net.legacyfabric.fabric.api.registry.v1;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapper;

/**
 * Allows registration of Blocks and Items.
 */
public final class RegistryHelper {
	/**
	 * Registers a block with the given ID.
	 *
	 * <p>The Block's translation key is automatically set.</p>
	 *
	 * @param block The block to register
	 * @param id    The ID of the block
	 * @return The block registered
	 */
	public static Block registerBlock(Block block, Identifier id) {
		return RegistryHelperImpl.registerBlock(block, id);
	}

	public static Block getBlock(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryRemapper.BLOCKS);
	}

	/**
	 * Registers an item with the given ID.
	 *
	 * <p>The Item's translation key is automatically set.</p>
	 *
	 * @param item The item to register
	 * @param id   The ID of the item
	 * @return The item registered
	 */
	public static Item registerItem(Item item, Identifier id) {
		return RegistryHelperImpl.registerItem(item, id);
	}

	public static Block getItem(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryRemapper.ITEMS);
	}

	/**
	 * Registers a block entity with the given ID.
	 *
	 * @param blockEntityClass The block entity class to register
	 * @param id    The ID of the block entity
	 * @return The block entity class registered
	 */
	public static Class<? extends BlockEntity> registerBlockEntity(Class<? extends BlockEntity> blockEntityClass, Identifier id) {
		return RegistryHelperImpl.registerBlockEntity(blockEntityClass, id);
	}

	public static Class<? extends BlockEntity> getBlockEntity(Identifier id) {
		return RegistryHelperImpl.getValue(id, RegistryRemapper.BLOCK_ENTITIES);
	}
}
