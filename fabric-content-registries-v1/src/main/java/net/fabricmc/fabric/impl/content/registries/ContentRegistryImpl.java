package net.fabricmc.fabric.impl.content.registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.mixin.content.registries.ItemAccessor;
import net.minecraft.util.Pair;

public final class ContentRegistryImpl {
	public static List<Pair<Identifier, Item>> unsortedItems = new ArrayList<>();
	public static List<Pair<Identifier, Block>> unsortedBlocks = new ArrayList<>();

	private ContentRegistryImpl() {
	}

	private static Block registerBlock(Identifier id, Block block, int i) {
		Block.REGISTRY.add(i, id, block);
		for (BlockState state : block.method_630().method_1228()) {
			Block.BLOCK_STATES.set(state, i << 4 | block.getData(state));
		}
		return block;
	}

	private static Item registerItem(Identifier id, Item item, int i) {
		Item.REGISTRY.add(i, id, item);
		return item;
	}

	public static void registerBlocks(HashMap<Identifier, Integer> idMap) {
		for(Pair<Identifier, Block> block : unsortedBlocks){
			int id = idMap.get(block.getLeft());
			registerBlock(block.getLeft(), block.getRight(), id);
		}
	}

	public static void registerItems(HashMap<Identifier, Integer> idMap) {
		for(Pair<Identifier, Item> item : unsortedItems){
			int id = idMap.get(item.getLeft());
			registerItem(item.getLeft(), item.getRight(), id);
		}
	}
}
