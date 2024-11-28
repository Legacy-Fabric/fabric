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

package net.legacyfabric.fabric.test.item.group;

import java.util.stream.StreamSupport;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.itemgroup.ItemGroup;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.legacyfabric.fabric.api.util.Identifier;

public class ItemGroupTest implements ModInitializer {
	//Adds an item group with all items in it
	private static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier("legacy-fabric-item-groups-v1-testmod", "test_group"))
			.iconWithItemStack(() -> new ItemStack(Items.DIAMOND))
			.appendItems(stacks ->
					StreamSupport.stream(Item.REGISTRY.spliterator(), false)
							.map(ItemStack::new)
							.forEach(stacks::add)
			).build();

	private static final ItemGroup ITEM_GROUP_2 = FabricItemGroupBuilder.create(new Identifier("legacy-fabric-item-groups-v1-testmod", "test_group_two"))
			.iconWithItemStack(() -> new ItemStack(Items.REDSTONE))
			.appendItems((stacks, itemGroup) -> {
				for (Item item : Item.REGISTRY) {
					if (item.getItemGroup() == ItemGroup.FOOD || item.getItemGroup() == itemGroup) {
						stacks.add(new ItemStack(item));
					}
				}
			}).build();

	@Override
	public void onInitialize() { }
}
