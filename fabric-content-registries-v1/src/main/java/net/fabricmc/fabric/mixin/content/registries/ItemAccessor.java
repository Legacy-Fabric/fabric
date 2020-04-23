package net.fabricmc.fabric.mixin.content.registries;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

@Mixin(Item.class)
public interface ItemAccessor {
	@Accessor("BLOCK_ITEMS")
	static Map<Block, Item> getBlockItems() {
		throw new AssertionError();
	}
}
