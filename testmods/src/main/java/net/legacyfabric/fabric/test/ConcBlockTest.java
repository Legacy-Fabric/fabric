/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.legacyfabric.fabric.test;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

public class ConcBlockTest implements ModInitializer {
	@Override
	public void onInitialize() {
		Block concBlock = new Block(Material.STONE, MaterialColor.BLUE).setItemGroup(ItemGroup.FOOD).setTranslationKey("conc_block");
		Item concItem = new BlockItem(concBlock);

		Block.REGISTRY.add(500, new Identifier("legacy-fabric", "conc_block"), concBlock);
		Item.REGISTRY.add(1000, new Identifier("legacy-fabric", "conc_block"), concItem);

		for (BlockState blockState : concBlock.getStateManager().getBlockStates()) {
			int blockStateId = Block.REGISTRY.getIndex(concBlock) << 4 | concBlock.getData(blockState);
			Block.BLOCK_STATES.set(blockState, blockStateId);
		}
	}
}
