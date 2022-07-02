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

package net.legacyfabric.fabric.impl.registry;

import java.util.IdentityHashMap;

import com.google.common.collect.BiMap;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;
import net.legacyfabric.fabric.mixin.registry.sync.ItemAccessor;

@ApiStatus.Internal
public class RegistryHelperImpl {
	private static final boolean hasFlatteningBegun = VersionUtils.matches(">=1.8 <=1.12.2");

	public static Block registerBlock(Block block, Identifier id) {
		block.setTranslationKey(formatTranslationKey(id));
		int rawId = nextId((SimpleRegistryCompat<Identifier, ?>) Block.REGISTRY);
		Block.REGISTRY.add(rawId, id, block);

		if (hasFlatteningBegun) {
			for (BlockState blockState : block.getStateManager().getBlockStates()) {
				int i = rawId << 4 | block.getData(blockState);
				Block.BLOCK_STATES.set(blockState, i);
			}
		}

		return block;
	}

	public static Item registerItem(Item item, Identifier id) {
		item.setTranslationKey(formatTranslationKey(id));
		int rawId = nextId((SimpleRegistryCompat<Identifier, ?>) Item.REGISTRY);
		Item.REGISTRY.add(rawId, id, item);

		if (hasFlatteningBegun) {
			if (item instanceof BlockItem) {
				ItemAccessor.getBLOCK_ITEMS().put(((BlockItem) item).getBlock(), item);
			}
		}

		return item;
	}

	private static String formatTranslationKey(Identifier key) {
		return key.getNamespace() + "." + key.getPath();
	}

	public static int nextId(SimpleRegistryCompat<Identifier, ?> registry) {
		int id = 0;

		while (getIdList(registry).fromInt(id) != null) {
			id++;
		}

		return id;
	}

	public static int nextId(IdListCompat<?> idList) {
		int id = 0;

		while (idList.fromInt(id) != null) {
			id++;
		}

		return id;
	}

	public static <T> IdListCompat<T> getIdList(SimpleRegistryCompat<Identifier, T> registry) {
		return registry.getIds();
	}

	public static <T> BiMap<T, Identifier> getObjects(SimpleRegistryCompat<Identifier, T> registry) {
		return (BiMap<T, Identifier>) registry.getObjects();
	}

	public static <T> IdentityHashMap<T, Integer> getIdMap(IdListCompat<T> idList, SimpleRegistryCompat<Identifier, T> registry) {
		return idList.getIdMap(registry);
	}

	public static <T> IdentityHashMap<T, Integer> getIdMap(SimpleRegistryCompat<Identifier, T> registry) {
		return getIdMap(getIdList(registry), registry);
	}
}
