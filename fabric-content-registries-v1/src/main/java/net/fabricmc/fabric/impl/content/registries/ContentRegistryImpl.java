package net.fabricmc.fabric.impl.content.registries;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.mixin.content.registries.ItemAccessor;

public final class ContentRegistryImpl {
	private static final AtomicInteger NEXT_BLOCK_ID = new AtomicInteger(198);
	private static final AtomicInteger NEXT_ITEM_ID = new AtomicInteger(198);

	private ContentRegistryImpl() {
	}

	public static Block registerBlock(Identifier id, Block block) {
		int index = nextBlockId();
		Block.REGISTRY.method_4951(index, id, block);

		for (BlockState state : block.method_630().method_1228()) {
			Block.blockStates.set(state, index << 4 | block.getData(state));
		}

		return block;
	}

	public static Item registerItem(Identifier id, Item item) {
		int index = nextItemId();
		Item.REGISTRY.method_4951(index, id, item);
		return item;
	}

	public static Item registerBlockItem(Identifier id, BlockItem item) {
		int index = Block.REGISTRY.getIndex(item.getBlock());
		Item.REGISTRY.method_4951(index, id, item);
		ItemAccessor.getBlockItems().put(item.getBlock(), item);
		return item;
	}

	public static int nextBlockId() {
		return NEXT_BLOCK_ID.getAndIncrement();
	}

	public static int nextItemId() {
		return NEXT_ITEM_ID.getAndIncrement();
	}
}
