package net.fabricmc.fabric.api.content.registries.v1;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.impl.content.registries.ContentRegistryImpl;

public final class BlockRegistry {
	private BlockRegistry() { }
	
	public static Block register(Identifier id, Block block) {
		return ContentRegistryImpl.registerBlock(id, block);
	}
}
