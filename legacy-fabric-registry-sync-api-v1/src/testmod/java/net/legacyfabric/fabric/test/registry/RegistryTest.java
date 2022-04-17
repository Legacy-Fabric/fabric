package net.legacyfabric.fabric.test.registry;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.registry.v1.RegistryHelper;

public class RegistryTest implements ModInitializer {
	@Override
	public void onInitialize() {
		Block concBlock = new Block(Material.STONE, MaterialColor.BLACK).setItemGroup(ItemGroup.FOOD);
		Block concBlock2 = new Block(Material.STONE, MaterialColor.BLUE).setItemGroup(ItemGroup.FOOD);
		Block[] blocks = ThreadLocalRandom.current().nextBoolean() ? new Block[] { concBlock, concBlock2 } : new Block[] { concBlock2, concBlock };
		for (Block block : blocks) {
			RegistryHelper.registerBlock(block, new Identifier("legacy-fabric-api", "conc_block_" + block.getMaterialColor(block.getDefaultState()).color));
			RegistryHelper.registerItem(new BlockItem(block), Block.REGISTRY.getIdentifier(block));
		}
	}
}
