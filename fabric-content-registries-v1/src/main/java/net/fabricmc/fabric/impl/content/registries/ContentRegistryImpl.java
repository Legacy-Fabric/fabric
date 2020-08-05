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

package net.fabricmc.fabric.impl.content.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.content.registry.v1.EntityRegistry;
import net.fabricmc.fabric.api.event.registry.v1.RegistryBlockAddedCallback;
import net.fabricmc.fabric.api.event.registry.v1.RegistryEntityAddedCallback;
import net.fabricmc.fabric.api.event.registry.v1.RegistryItemAddedCallback;
import net.fabricmc.fabric.mixin.content.registries.BlockAccessor;
import net.fabricmc.fabric.mixin.content.registries.EntityTypeAccessor;
import net.fabricmc.fabric.mixin.content.registries.MutableRegistryAccessor;
import net.fabricmc.fabric.mixin.content.registries.SimpleRegistryAccessor;

public final class ContentRegistryImpl implements ModInitializer {
	public static Map<Identifier, Item> unsortedItems = new HashMap<>();
	public static Map<Identifier, Block> unsortedBlocks = new HashMap<>();
	public static Map<String, Class<? extends Entity>> unsortedEntities = new HashMap<>();
	public static IdList<Pair<Identifier, Block>> vanillaBlocks = new IdList<>();
	public static IdList<Pair<Identifier, Item>> vanillaItems = new IdList<>();
	public static IdList<Pair<String, Class<? extends Entity>>> vanillaEntities = new IdList<>();
	public static IdList<BlockState> vanillaBlockStates = new IdList<>();
	public static int unorderedNextBlockId = 198;
	public static int unorderedNextItemId = 4096;
	public static int unorderedNextEntityId = 201;
	public static Map<String, Class<? extends Entity>> moddedEntities = new HashMap<>();

	static {
		preserveVanillaEntries();
	}

	private static void preserveVanillaEntries() {
		for (Block block : Block.REGISTRY) {
			vanillaBlocks.set(new Pair<>(Block.REGISTRY.getIdentifier(block), block), Block.REGISTRY.getIndex(block));
		}

		for (Item item : Item.REGISTRY) {
			vanillaItems.set(new Pair<>(Item.REGISTRY.getIdentifier(item), item), Item.REGISTRY.getIndex(item));
		}

		for (BlockState state : Block.BLOCK_STATES) {
			vanillaBlockStates.set(state, Block.BLOCK_STATES.getId(state));
		}

		for (Map.Entry<String, Class<? extends Entity>> entry : EntityTypeAccessor.getNameClassMap().entrySet()) {
			vanillaEntities.set(new Pair<>(entry.getKey(), entry.getValue()), EntityTypeAccessor.getNameIdMap().get(entry.getKey()));
		}
	}

	public static <T extends Block> T registerBlock(Identifier id, T block) {
		RegistryBlockAddedCallback.EVENT.invoker().blockAdded(id, block);
		unsortedBlocks.put(id, block);
		Block.REGISTRY.add(unorderedNextBlockId, id, block);

		for (BlockState state : block.getStateManager().method_1228()) {
			Block.BLOCK_STATES.set(state, unorderedNextBlockId << 4 | block.getData(state));
		}

		unorderedNextBlockId++;
		return block;
	}

	public static <T extends Item> T registerItem(Identifier id, T item) {
		RegistryItemAddedCallback.EVENT.invoker().itemAdded(id, item);
		unsortedItems.put(id, item);
		Item.REGISTRY.add(unorderedNextItemId, id, item);
		unorderedNextItemId++;
		return item;
	}

	public static void registerEntity(Class<? extends Entity> clazz, String name) {
		RegistryEntityAddedCallback.EVENT.invoker().entityAdded(clazz, name);
		unsortedEntities.put(name, clazz);
		EntityTypeAccessor.invokeRegisterEntity(clazz, name, unorderedNextEntityId);
		moddedEntities.put(name, clazz);
		unorderedNextEntityId++;
	}

	public static void fillBlocksMapWithUnknownEntries(BiMap<Integer, Identifier> idMap) {
		idMap.values().removeIf(identifier -> !unsortedBlocks.containsKey(identifier));

		for (Map.Entry<Identifier, Block> entry : unsortedBlocks.entrySet()) {
			if (!idMap.containsValue(entry.getKey())) {
				int id = nextAvailableBlockId(idMap);
				idMap.put(id, entry.getKey());
			}
		}
	}

	public static void fillItemsMapWithUnknownEntries(BiMap<Integer, Identifier> idMap) {
		idMap.values().removeIf(identifier -> !unsortedBlocks.containsKey(identifier));

		for (Map.Entry<Identifier, Item> entry : unsortedItems.entrySet()) {
			if (!idMap.containsValue(entry.getKey())) {
				int id = nextAvailableItemId(idMap);
				idMap.put(id, entry.getKey());
			}
		}
	}

	public static void fillEntitiesMapWithUnknownEntries(BiMap<Integer, String> idMap) {
		idMap.values().removeIf(string -> !unsortedEntities.containsKey(string));

		for (Map.Entry<String, Class<? extends Entity>> entry : unsortedEntities.entrySet()) {
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

		for (Pair<Identifier, Block> pair : vanillaBlocks) {
			Block.REGISTRY.add(vanillaBlocks.getId(pair), pair.getLeft(), pair.getRight());
		}

		IdList<BlockState> states = new IdList<>();

		for (BlockState state : vanillaBlockStates) {
			states.set(state, vanillaBlockStates.getId(state));
		}

		for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
			Block block = unsortedBlocks.get(entry.getValue());
			Block.REGISTRY.add(entry.getKey(), entry.getValue(), block);

			for (BlockState state : block.getStateManager().method_1228()) {
				states.set(state, unorderedNextBlockId << 4 | block.getData(state));
			}
		}

		BlockAccessor.setBlockStates(states);
	}

	public static void reorderItemEntries(BiMap<Integer, Identifier> idMap) {
		((MutableRegistryAccessor) Item.REGISTRY).getMap().clear();
		((SimpleRegistryAccessor) Item.REGISTRY).setIds(new IdList<Item>());
		((SimpleRegistryAccessor) Item.REGISTRY).getObjects().clear();

		for (Pair<Identifier, Item> pair : vanillaItems) {
			Item.REGISTRY.add(vanillaItems.getId(pair), pair.getLeft(), pair.getRight());
		}

		for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
			Item.REGISTRY.add(entry.getKey(), entry.getValue(), unsortedItems.get(entry.getValue()));
		}
	}

	public static void reorderEntityEntries(BiMap<Integer, String> idMap) {
		EntityTypeAccessor.getNameIdMap().clear();
		EntityTypeAccessor.getClassIdMap().clear();
		EntityTypeAccessor.getIdClassMap().clear();
		EntityTypeAccessor.setNameIdMap(Maps.newHashMap());
		EntityTypeAccessor.setClassIdMap(Maps.newHashMap());
		EntityTypeAccessor.setIdClassMap(Maps.newHashMap());

		for (Pair<String, Class<? extends Entity>> pair : vanillaEntities) {
			EntityTypeAccessor.getNameIdMap().put(pair.getLeft(), vanillaEntities.getId(pair));
			EntityTypeAccessor.getClassIdMap().put(pair.getRight(), vanillaEntities.getId(pair));
			EntityTypeAccessor.getIdClassMap().put(vanillaEntities.getId(pair), pair.getRight());
		}

		for (Map.Entry<Integer, String> entry : idMap.entrySet()) {
			EntityTypeAccessor.getNameIdMap().put(entry.getValue(), entry.getKey());
			EntityTypeAccessor.getClassIdMap().put(moddedEntities.get(entry.getValue()), entry.getKey());
			EntityTypeAccessor.getIdClassMap().put(entry.getKey(), moddedEntities.get(entry.getValue()));
		}
	}

	@Override
	public void onInitialize() {
	}
}
