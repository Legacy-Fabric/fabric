/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.test.registry;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.itemgroup.ItemGroup;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.resource.ItemModelRegistry;
import net.legacyfabric.fabric.api.util.Identifier;

public class RegistryTest implements ModInitializer {
	@Override
	public void onInitialize() {
		this.registerItems();
		this.registerBlocks();
	}

	private void registerItems() {
		Item testItem = new Item().setItemGroup(ItemGroup.FOOD);
		RegistryHelper.register(
				Item.REGISTRY,
				new Identifier("legacy-fabric-api", "test_item"), testItem
		);
		ItemModelRegistry.registerItemModel(testItem, new Identifier("legacy-fabric-api:test_item"));
	}

	private void registerBlocks() {
		Block concBlock = new Block(Material.STONE, MaterialColor.BLACK).setItemGroup(ItemGroup.FOOD);
		Block concBlock2 = new Block(Material.GLASS, MaterialColor.BLUE).setItemGroup(ItemGroup.FOOD);
		Block[] blocks = ThreadLocalRandom.current().nextBoolean() ? new Block[]{concBlock, concBlock2} : new Block[]{concBlock2, concBlock};

		for (Block block : blocks) {
			Identifier identifier = new Identifier("legacy-fabric-api:conc_block_" + block.getMaterialColor(block.getDefaultState()).color);
			RegistryHelper.register(Block.REGISTRY, identifier, block);
			RegistryHelper.register(Item.REGISTRY, identifier, new BlockItem(block));
		}
	}
}
