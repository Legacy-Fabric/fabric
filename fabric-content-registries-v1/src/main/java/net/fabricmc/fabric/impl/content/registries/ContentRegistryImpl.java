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

package net.fabricmc.fabric.impl.content.registries;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.v1.FabricRegistryEntryAddedEvents;
import net.fabricmc.fabric.mixin.content.registries.MutableRegistryAccessor;
import net.fabricmc.fabric.mixin.content.registries.BlockAccessor;
import net.fabricmc.fabric.mixin.content.registries.BlockEntityAccessor;
import net.fabricmc.fabric.mixin.content.registries.EntityTypeAccessor;
import net.fabricmc.fabric.mixin.content.registries.SimpleRegistryAccessor;

public final class ContentRegistryImpl implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<String, Class<? extends Entity>> MODDED_ENTITIES = new HashMap<>();
	private static final Map<Identifier, Item> UNSORTED_ITEMS = new HashMap<>();
	private static final Map<Identifier, Block> UNSORTED_BLOCKS = new HashMap<>();
	private static final Map<String, Class<? extends Entity>> UNSORTED_ENTITIES = new HashMap<>();
	private static final IdList<Pair<Identifier, Block>> VANILLA_BLOCKS = new IdList<>();
	private static final IdList<Pair<Identifier, Item>> VANILLA_ITEMS = new IdList<>();
	private static final IdList<BlockState> VANILLA_BLOCK_STATES = new IdList<>();
	private static final IdList<Pair<String, Class<? extends Entity>>> VANILLA_ENTITIES = new IdList<>();
	private static int unorderedNextBlockId = 198;
	private static int unorderedNextItemId = 4096;
	private static int unorderedNextEntityId = 201;
	public static boolean blockIdsSetup = false;
	public static boolean itemIdsSetup = false;
	public static boolean entityIdsSetup = false;

	static {
		preserveVanillaEntries();
	}

	private static void preserveVanillaEntries() {
		for (Block block : Block.REGISTRY) {
			VANILLA_BLOCKS.set(new Pair<>(Block.REGISTRY.getIdentifier(block), block), Block.REGISTRY.getIndex(block));
		}

		for (Item item : Item.REGISTRY) {
			VANILLA_ITEMS.set(new Pair<>(Item.REGISTRY.getIdentifier(item), item), Item.REGISTRY.getIndex(item));
		}

		for (BlockState state : Block.BLOCK_STATES) {
			VANILLA_BLOCK_STATES.set(state, Block.BLOCK_STATES.getId(state));
		}

		for (Map.Entry<String, Class<? extends Entity>> entry : EntityTypeAccessor.getNameClassMap().entrySet()) {
			VANILLA_ENTITIES.set(new Pair<>(entry.getKey(), entry.getValue()), EntityTypeAccessor.getNameIdMap().get(entry.getKey()));
		}
	}

	public static <T extends Block> T registerBlock(Identifier id, T block) {
		FabricRegistryEntryAddedEvents.BLOCK.invoker().blockAdded(id, block);
		UNSORTED_BLOCKS.put(id, block);
		Block.REGISTRY.add(unorderedNextBlockId, id, block);

		for (BlockState state : block.getStateManager().getBlockStates()) {
			Block.BLOCK_STATES.set(state, unorderedNextBlockId << 4 | block.getData(state));
		}

		unorderedNextBlockId++;
		return block;
	}

	public static <T extends Item> T registerItem(Identifier id, T item) {
		FabricRegistryEntryAddedEvents.ITEM.invoker().itemAdded(id, item);
		UNSORTED_ITEMS.put(id, item);
		Item.REGISTRY.add(unorderedNextItemId, id, item);
		unorderedNextItemId++;
		return item;
	}

	public static void registerEntity(Class<? extends Entity> clazz, String name) {
		if (EntityTypeAccessor.getNameClassMap().containsKey(name)) {
			throw new UnsupportedOperationException(String.format("Duplicate entity name %s found of class %s. If you're trying to overwrite an entity, you can't yet.", name, clazz.getName()));
		} else if (EntityTypeAccessor.getNameClassMap().containsValue(clazz)) {
			throw new UnsupportedOperationException(String.format("Duplicate entity class %s found of name %s. If you're trying to overwrite an entity, you can't yet.", clazz.getName(), name));
		}

		FabricRegistryEntryAddedEvents.ENTITY.invoker().entityAdded(clazz, name);
		UNSORTED_ENTITIES.put(name, clazz);
		EntityTypeAccessor.invokeRegisterEntity(clazz, name, unorderedNextEntityId);
		MODDED_ENTITIES.put(name, clazz);
		unorderedNextEntityId++;
	}

	public static void registerBlockEntity(Class<? extends BlockEntity> clazz, String name) {
		if (BlockEntityAccessor.getStringClassMap().containsKey(name)) {
			throw new UnsupportedOperationException(String.format("Duplicate block entity name %s found of class %s. If you're trying to overwrite a block entity, you can't yet.", name, clazz.getName()));
		} else if (BlockEntityAccessor.getStringClassMap().containsValue(clazz)) {
			throw new UnsupportedOperationException(String.format("Duplicate block entity class %s found of name %s. If you're trying to overwrite a block entity, you can't yet.", clazz.getName(), name));
		}

		BlockEntityAccessor.invokeRegisterBlockEntity(clazz, name);
		FabricRegistryEntryAddedEvents.BLOCK_ENTITY.invoker().blockEntityAdded(clazz, name);
	}

	public static void fillBlocksMapWithUnknownEntries(BiMap<Integer, Identifier> idMap) {
		idMap.values().removeIf(identifier -> !UNSORTED_BLOCKS.containsKey(identifier));

		for (Map.Entry<Identifier, Block> entry : UNSORTED_BLOCKS.entrySet()) {
			if (!idMap.containsValue(entry.getKey())) {
				int id = nextAvailableBlockId(idMap);
				idMap.put(id, entry.getKey());
			}
		}
	}

	public static void fillItemsMapWithUnknownEntries(BiMap<Integer, Identifier> idMap) {
		idMap.values().removeIf(identifier -> !UNSORTED_BLOCKS.containsKey(identifier));

		for (Map.Entry<Identifier, Item> entry : UNSORTED_ITEMS.entrySet()) {
			if (!idMap.containsValue(entry.getKey())) {
				int id = nextAvailableItemId(idMap);
				idMap.put(id, entry.getKey());
			}
		}
	}

	public static void fillEntitiesMapWithUnknownEntries(BiMap<Integer, String> idMap) {
		idMap.values().removeIf(string -> !UNSORTED_ENTITIES.containsKey(string));

		for (Map.Entry<String, Class<? extends Entity>> entry : UNSORTED_ENTITIES.entrySet()) {
			if (!idMap.containsValue(entry.getKey())) {
				int id = nextAvailableEntityId(idMap);
				idMap.put(id, entry.getKey());
			}
		}
	}

	private static int nextAvailableBlockId(Map<Integer, Identifier> idMap) {
		int i = 198;

		while (true) {
			if (!idMap.containsKey(i)) {
				return i;
			}

			i++;
		}
	}

	private static int nextAvailableItemId(Map<Integer, Identifier> idMap) {
		int i = 432;

		while (true) {
			if ((i < 2256 || i > 2267) && !idMap.containsKey(i)) {
				return i;
			}

			i++;
		}
	}

	private static int nextAvailableEntityId(Map<Integer, String> idMap) {
		int i = 201;

		while (true) {
			if (!idMap.containsKey(i)) {
				return i;
			}

			i++;
		}
	}

	public static void reorderBlockEntries(BiMap<Integer, Identifier> idMap) {
		((MutableRegistryAccessor) Block.REGISTRY).getMap().clear();
		((SimpleRegistryAccessor) Block.REGISTRY).setIds(new IdList<Block>());
		((SimpleRegistryAccessor) Block.REGISTRY).getObjects().clear();

		for (Pair<Identifier, Block> pair : VANILLA_BLOCKS) {
			Block.REGISTRY.add(VANILLA_BLOCKS.getId(pair), pair.getLeft(), pair.getRight());
		}

		IdList<BlockState> states = new IdList<>();

		for (BlockState state : VANILLA_BLOCK_STATES) {
			states.set(state, VANILLA_BLOCK_STATES.getId(state));
		}

		for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
			Block block = UNSORTED_BLOCKS.get(entry.getValue());
			Block.REGISTRY.add(entry.getKey(), entry.getValue(), block);

			for (BlockState state : block.getStateManager().getBlockStates()) {
				states.set(state, unorderedNextBlockId << 4 | block.getData(state));
			}
		}

		BlockAccessor.setBlockStates(states);
	}

	public static void reorderItemEntries(BiMap<Integer, Identifier> idMap) {
		((MutableRegistryAccessor) Item.REGISTRY).getMap().clear();
		((SimpleRegistryAccessor) Item.REGISTRY).setIds(new IdList<Item>());
		((SimpleRegistryAccessor) Item.REGISTRY).getObjects().clear();

		for (Pair<Identifier, Item> pair : VANILLA_ITEMS) {
			Item.REGISTRY.add(VANILLA_ITEMS.getId(pair), pair.getLeft(), pair.getRight());
		}

		for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
			Item.REGISTRY.add(entry.getKey(), entry.getValue(), UNSORTED_ITEMS.get(entry.getValue()));
		}
	}

	public static void reorderEntityEntries(BiMap<Integer, String> idMap) {
		EntityTypeAccessor.getNameIdMap().clear();
		EntityTypeAccessor.getClassIdMap().clear();
		EntityTypeAccessor.getIdClassMap().clear();
		EntityTypeAccessor.setNameIdMap(Maps.newHashMap());
		EntityTypeAccessor.setClassIdMap(Maps.newHashMap());
		EntityTypeAccessor.setIdClassMap(Maps.newHashMap());

		for (Pair<String, Class<? extends Entity>> pair : VANILLA_ENTITIES) {
			EntityTypeAccessor.getNameIdMap().put(pair.getLeft(), VANILLA_ENTITIES.getId(pair));
			EntityTypeAccessor.getClassIdMap().put(pair.getRight(), VANILLA_ENTITIES.getId(pair));
			EntityTypeAccessor.getIdClassMap().put(VANILLA_ENTITIES.getId(pair), pair.getRight());
		}

		for (Map.Entry<Integer, String> entry : idMap.entrySet()) {
			EntityTypeAccessor.getNameIdMap().put(entry.getValue(), entry.getKey());
			EntityTypeAccessor.getClassIdMap().put(MODDED_ENTITIES.get(entry.getValue()), entry.getKey());
			EntityTypeAccessor.getIdClassMap().put(entry.getKey(), MODDED_ENTITIES.get(entry.getValue()));
		}
	}

	@Override
	public void onInitialize() {
	}
}
