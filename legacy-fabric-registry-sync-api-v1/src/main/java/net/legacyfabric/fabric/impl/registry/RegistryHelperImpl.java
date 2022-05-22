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

package net.legacyfabric.fabric.impl.registry;

import java.util.IdentityHashMap;

import com.google.common.collect.BiMap;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.mixin.registry.sync.IdListAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.ItemAccessor;
import net.legacyfabric.fabric.mixin.registry.sync.SimpleRegistryAccessor;

@ApiStatus.Internal
public class RegistryHelperImpl {
	public static Block registerBlock(Block block, Identifier id) {
		block.setTranslationKey(formatTranslationKey(id));
		int rawId = nextId(Block.REGISTRY);
		Block.REGISTRY.add(rawId, id, block);

		for (Object blockStateObject : block.getStateManager().getBlockStates()) {
			BlockState blockState = (BlockState) blockStateObject;
			int i = rawId << 4 | block.getData(blockState);
			Block.BLOCK_STATES.set(blockState, i);
		}

		return block;
	}

	public static Item registerItem(Item item, Identifier id) {
		item.setTranslationKey(formatTranslationKey(id));
		int rawId = nextId(Item.REGISTRY);
		Item.REGISTRY.add(rawId, id, item);

		if (item instanceof BlockItem) {
			ItemAccessor.getBLOCK_ITEMS().put(((BlockItem) item).getBlock(), item);
		}

		return item;
	}

	private static String formatTranslationKey(Identifier key) {
		return key.getNamespace() + "." + key.getPath();
	}

	public static int nextId(SimpleRegistry registry) {
		int id = 0;

		while (getIdList(registry).fromId(id) != null) {
			id++;
		}

		return id;
	}

	public static IdList getIdList(SimpleRegistry registry) {
		return ((SimpleRegistryAccessor) registry).getIds();
	}

	public static BiMap<?, Identifier> getObjects(SimpleRegistry registry) {
		//noinspection unchecked
		return (BiMap<?, Identifier>) ((SimpleRegistryAccessor) registry).getObjects();
	}

	public static IdentityHashMap<?, Integer> getIdMap(IdList idList) {
		return ((IdListAccessor) idList).getIdMap();
	}

	public static IdentityHashMap<?, Integer> getIdMap(SimpleRegistry registry) {
		return getIdMap(getIdList(registry));
	}
}
