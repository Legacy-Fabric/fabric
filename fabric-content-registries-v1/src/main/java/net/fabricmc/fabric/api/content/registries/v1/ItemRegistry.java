package net.fabricmc.fabric.api.content.registries.v1;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.impl.content.registries.ContentRegistryImpl;

public final class ItemRegistry {
	private ItemRegistry() {
	}

	public static Item register(Identifier id, Item item) {
		return ContentRegistryImpl.registerItem(id, item);
	}

	public static Item registerBlockItem(Identifier id, Item item) {
		return ContentRegistryImpl.registerBlockItem(id, (BlockItem) item);
	}
}

