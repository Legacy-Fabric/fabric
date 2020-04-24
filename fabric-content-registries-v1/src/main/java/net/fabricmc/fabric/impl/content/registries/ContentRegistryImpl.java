package net.fabricmc.fabric.impl.content.registries;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.mixin.content.registries.BlockAccessor;
import net.fabricmc.fabric.mixin.content.registries.MutableRegistryAccessor;
import net.fabricmc.fabric.mixin.content.registries.SimpleRegistryAccessor;

public final class ContentRegistryImpl implements ModInitializer {
	public static Map<Identifier, Item> unsortedItems = new HashMap<>();
	public static Map<Identifier, Block> unsortedBlocks = new HashMap<>();
	public static IdList<Pair<Identifier, Block>> vanillaBlocks = new IdList<>();
	public static IdList<Pair<Identifier, Item>> vanillaItems = new IdList<>();
	public static IdList<BlockState> vanillaBlockStates = new IdList<>();
	public static int unorderedNextBlockId = 198;
	public static int unorderedNextItemId = 4096;

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
	}

	public static Block registerBlock(Identifier id, Block block) {
		unsortedBlocks.put(id, block);
		Block.REGISTRY.add(unorderedNextBlockId, id, block);

		for (BlockState state : block.method_630().method_1228()) {
			Block.BLOCK_STATES.set(state, unorderedNextBlockId << 4 | block.getData(state));
		}

		unorderedNextBlockId++;
		return block;
	}

	public static Item registerItem(Identifier id, Item item) {
		unsortedItems.put(id, item);
		Item.REGISTRY.add(unorderedNextItemId, id, item);
		unorderedNextItemId++;
		return item;
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

			for (BlockState state : block.method_630().method_1228()) {
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

	@Override
	public void onInitialize() {
	}
}
