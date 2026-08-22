/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.ornithemc.osl.blocks.api.BlockEvents;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.items.api.ItemEvents;
import net.ornithemc.osl.items.api.ItemRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.CreativeModeTab;
import net.minecraft.item.Item;

public class RegistryTest implements ModInitializer {
	@Override
	public void init() {
		ItemEvents.REGISTER_ITEMS.register(this::registerItems);
		this.registerBlocks();
	}

	private void registerItems() {
		Item testItem = new TestItem()
				.setCreativeModeTab(CreativeModeTab.FOOD)
				.setSpriteName(NamespacedIdentifiers.from("legacy-fabric-api", "test_item").toString());
		ItemRegistry.register(
				NamespacedIdentifiers.from("legacy-fabric-api", "test_item"), testItem
		);
	}

	private void registerBlocks() {
		List<Object> blockList = new ArrayList<>();

		BlockEvents.REGISTER_BLOCKS.register(() -> {
			Block concBlock = new TestBlock(Material.STONE).setCreativeModeTab(CreativeModeTab.FOOD);
			Block concBlock2 = new TestBlock(Material.GLASS).setCreativeModeTab(CreativeModeTab.FOOD);
			Block[] blocks = ThreadLocalRandom.current().nextBoolean() ? new Block[]{concBlock, concBlock2} : new Block[]{concBlock2, concBlock};

			for (Block block : blocks) {
				NamespacedIdentifier identifier = NamespacedIdentifiers.from("legacy-fabric-api", "conc_block_" + block.material.color.color);

				BlockRegistry.register(identifier, block);
				blockList.add(block);
			}
		});

		ItemEvents.REGISTER_BLOCK_ITEMS.register(() -> {
			for (Object o : blockList) {
				BlockItem item = ItemRegistry.register((Block) o);
			}
		});
	}
}
