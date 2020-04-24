package net.fabricmc.fabric.impl.content.registries;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.content.registries.v1.BlockRegistry;
import net.fabricmc.fabric.api.content.registries.v1.ItemRegistry;

public class TestMod implements ModInitializer {
	@Override
	public void onInitialize() {
		Block block = new Block(Material.DIRT, MaterialColor.YELLOW).setTranslationKey("test:cool_item").setItemGroup(ItemGroup.FOOD);
		BlockRegistry.register(new Identifier(block.getTranslationKey()), block);
		Item item = new BlockItem(block).setItemGroup(ItemGroup.FOOD);
		ItemRegistry.registerBlockItem(new Identifier(block.getTranslationKey()), item);
	}
}
