/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.api.effect;

import com.google.common.base.Predicate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;

import net.legacyfabric.fabric.impl.effect.versioned.PotionHelperImpl;

public interface PotionHelper {
	/**
	 * Register a potion item type.
	 * @param potionItem Potion item type instance
	 */
	static void registerPotionType(Item potionItem) {
		PotionHelperImpl.registerPotionType(potionItem);
	}

	/**
	 * Register a potion item type.
	 * @param potionItem Potion item type instance
	 * @param meta Item meta
	 */
	static void registerPotionType(Item potionItem, int meta) {
		PotionHelperImpl.registerPotionType(potionItem, meta);
	}

	/**
	 * Register a brewing recipe for a potion type.
	 * @param basePotion Base potion item
	 * @param ingredient Ingredient
	 * @param resultingPotion Resulting potion item
	 */
	static void registerPotionTypeRecipe(PotionItem basePotion, Item ingredient, PotionItem resultingPotion) {
		PotionHelperImpl.registerPotionTypeRecipe(basePotion, ingredient, resultingPotion);
	}

	/**
	 * Register a brewing recipe for a potion type.
	 * @param basePotion Base potion item
	 * @param ingredient Ingredient
	 * @param meta Ingredient meta
	 * @param resultingPotion Resulting potion item
	 */
	static void registerPotionTypeRecipe(PotionItem basePotion, Item ingredient, int meta, PotionItem resultingPotion) {
		PotionHelperImpl.registerPotionTypeRecipe(basePotion, ingredient, meta, resultingPotion);
	}

	/**
	 * Register a brewing recipe for a potion.
	 * @param basePotion Base potion
	 * @param ingredient Ingredient
	 * @param resultingPotion Resulting potion
	 */
	static void registerPotionRecipe(Potion basePotion, Item ingredient, Potion resultingPotion) {
		PotionHelperImpl.registerPotionRecipe(basePotion, ingredient, resultingPotion);
	}

	/**
	 * Register a brewing recipe for a potion.
	 * @param basePotion Base potion
	 * @param ingredient Ingredient
	 * @param meta Ingredient meta
	 * @param resultingPotion Resulting potion
	 */
	static void registerPotionRecipe(Potion basePotion, Item ingredient, int meta, Potion resultingPotion) {
		PotionHelperImpl.registerPotionRecipe(basePotion, ingredient, resultingPotion);
	}

	/**
	 * Register a brewing recipe for a potion.
	 * @param basePotion Base potion
	 * @param ingredientPredicate Ingredient predicate
	 * @param resultingPotion Resulting potion
	 */
	static void registerPotionRecipe(Potion basePotion, Predicate<ItemStack> ingredientPredicate, Potion resultingPotion) {
		PotionHelperImpl.registerPotionRecipe(basePotion, ingredientPredicate, resultingPotion);
	}
}
