/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.recipes.v1;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class RecipeEvents {
	public static final Event<ItemCrafted> ITEM_CRAFTED = EventFactory.createArrayBacked(ItemCrafted.class, (listeners) -> (stack, craftingInventory, playerEntity) -> {
		for (ItemCrafted callback : listeners) {
			callback.onCraft(stack, craftingInventory, playerEntity);
		}
	});

	@FunctionalInterface
	public interface ItemCrafted {
		/**
		 * Called when an item is crafted in a crafting table.
		 *
		 * @param stack the {@link ItemStack} that is the output of the crafting recipe
		 * @param craftingInventory the {@link CraftingInventory} that will still contain the ingredients for the crafting recipe
		 * @param playerEntity the {@link PlayerEntity} who is crafting the item
		 */
		void onCraft(ItemStack stack, CraftingInventory craftingInventory, PlayerEntity playerEntity);
	}
}
