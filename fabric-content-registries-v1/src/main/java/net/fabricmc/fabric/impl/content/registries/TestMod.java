package net.fabricmc.fabric.impl.content.registries;

import java.util.UUID;

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
	private static boolean a = false;

	@Override
	public void onInitialize() {

		System.out.println("dadawdaw");
		if (!a) {
			Block block = new Block(Material.DIRT, MaterialColor.YELLOW).setTranslationKey("test:cool_item").setItemGroup(ItemGroup.FOOD);
			BlockRegistry.register(new Identifier(block.getTranslationKey()), block);
			Item item = new BlockItem(block).setItemGroup(ItemGroup.FOOD);
			ItemRegistry.registerBlockItem(new Identifier(block.getTranslationKey()), item);
			for (int i = 0; i < 5; i++) {
				String s = UUID.randomUUID().toString();
				ItemRegistry.register(new Identifier(s), new Item().setTranslationKey(s).setItemGroup(ItemGroup.FOOD));
			}
			ItemRegistry.register(new Identifier("dada"), new Item().setTranslationKey("dada").setItemGroup(ItemGroup.FOOD));
		}
		a = true;
//		Item.REGISTRY.add(4096, new Identifier("dada"), new Item().setTranslationKey("a:a").setItemGroup(ItemGroup.FOOD));
	}
}
