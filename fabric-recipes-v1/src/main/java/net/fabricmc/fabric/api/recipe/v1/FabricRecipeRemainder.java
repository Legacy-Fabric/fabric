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

package net.fabricmc.fabric.api.recipe.v1;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface FabricRecipeRemainder {
	/**
	 * An {@link ItemStack} aware version of {@link Item#getRecipeRemainder()}.
	 *
	 * @param stack The input {@link ItemStack} for the current item. Can have metadata values
	 * @param craftingInventory the {@link CraftingInventory} that the stack is part of
	 * @param playerEntity the {@link PlayerEntity} that is crafting the item
	 * @return The {@link ItemStack} to remain in the crafting inventory
	 */
	ItemStack getRecipeRemainder(ItemStack stack, CraftingInventory craftingInventory, PlayerEntity playerEntity);
}
