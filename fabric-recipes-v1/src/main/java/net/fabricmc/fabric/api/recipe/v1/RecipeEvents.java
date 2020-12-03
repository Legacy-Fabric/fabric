/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.api.recipe.v1;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;

public class RecipeEvents {
	public static final Event<ItemCrafted> ITEM_CRAFTED = EventFactory.createArrayBacked(ItemCrafted.class, (listeners) -> (stack, craftingInventory, playerEntity) -> {
		for (ItemCrafted callback : listeners) {
			ActionResult result = callback.onCraft(stack, craftingInventory, playerEntity);

			if (result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.PASS;
	});

	@FunctionalInterface
	public interface ItemCrafted {
		/**
		 * Called when an item is crafted in a crafting table.
		 *
		 * <p>Upon return:
		 * <ul><li>SUCCESS cancels further processing and executes {@link Item#onCraft}.
		 * <li>PASS falls back to further processing.
		 * <li>FAIL cancels further processing and does not execute {@link Item#onCraft}.</ul>
		 *
		 * @param stack the {@link ItemStack} that is the output of the crafting recipe
		 * @param craftingInventory the {@link CraftingInventory} that will still contain the ingredients for the crafting recipe
		 * @param playerEntity the {@link PlayerEntity} who is crafting the item
		 */
		ActionResult onCraft(ItemStack stack, CraftingInventory craftingInventory, PlayerEntity playerEntity);
	}
}
