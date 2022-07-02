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

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.compat.BlockCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.IdListCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.ItemCompat;
import net.legacyfabric.fabric.impl.registry.sync.compat.RegistriesGetter;
import net.legacyfabric.fabric.impl.registry.sync.compat.SimpleRegistryCompat;

@ApiStatus.Internal
public class RegistryHelperImpl {
	private static final boolean hasFlatteningBegun = VersionUtils.matches(">=1.8 <=1.12.2");
	public static RegistriesGetter registriesGetter = null;

	public static Block registerBlock(Block block, Identifier id) {
		block.setTranslationKey(formatTranslationKey(id));
		int rawId = nextId(((BlockCompat) block).getRegistry());
		((BlockCompat) block).addToRegistry(rawId, id, block);

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
		int rawId = nextId(((ItemCompat) item).getRegistry());
		((ItemCompat) item).addToRegistry(rawId, id, item);

		if (hasFlatteningBegun) {
			if (item instanceof BlockItem) {
				((ItemCompat) item).getBLOCK_ITEMS().put(((BlockItem) item).getBlock(), item);
			}
		}

		return item;
	}

	private static String formatTranslationKey(Identifier key) {
		return key.getNamespace() + "." + key.getPath();
	}

	public static int nextId(SimpleRegistryCompat<?, ?> registry) {
		int id = 0;

		Identifier registryId = RegistryRemapper.getRegistryRemapper(registry).registryId;

		while (getIdList(registry).fromInt(id) != null
				|| (id < 256
				&& (registryId.equals(RegistryRemapper.BLOCKS) || registryId.equals(RegistryRemapper.ITEMS)))) {
			id++;
		}

		return id;
	}

	public static int nextId(IdListCompat<?> idList, SimpleRegistryCompat<?, ?> registry) {
		int id = 0;

		Identifier registryId = RegistryRemapper.getRegistryRemapper(registry).registryId;

		while (idList.fromInt(id) != null
				|| (id < 256
				&& (registryId.equals(RegistryRemapper.BLOCKS) || registryId.equals(RegistryRemapper.ITEMS)))) {
			id++;
		}

		return id;
	}

	public static <K, V> IdListCompat<V> getIdList(SimpleRegistryCompat<K, V> registry) {
		return registry.getIds();
	}

	public static <K, V> BiMap<V, K> getObjects(SimpleRegistryCompat<K, V> registry) {
		return (BiMap<V, K>) registry.getObjects();
	}

	public static <K, V> IdentityHashMap<V, Integer> getIdMap(IdListCompat<V> idList, SimpleRegistryCompat<K, V> registry) {
		return idList.getIdMap(registry);
	}

	public static <K, V> IdentityHashMap<V, Integer> getIdMap(SimpleRegistryCompat<K, V> registry) {
		return getIdMap(getIdList(registry), registry);
	}
}
