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

package net.legacyfabric.fabric.test.item.group;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.itemgroup.v1.FabricItemGroup;
import net.legacyfabric.fabric.api.itemgroup.v1.FabricItemGroupBuilder;

public class ItemGroupTest implements ModInitializer {
	public static final FabricItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier("dmn", "testgroup"), () -> Items.POTATO);

	@Override
	public void onInitialize() {
		GROUP.addStack(new ItemStack(Item.fromBlock(Blocks.COMMAND_BLOCK)));
	}
}
