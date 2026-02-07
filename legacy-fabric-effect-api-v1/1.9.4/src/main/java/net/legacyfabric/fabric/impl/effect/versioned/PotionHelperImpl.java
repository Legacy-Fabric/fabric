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

package net.legacyfabric.fabric.impl.effect.versioned;

import com.google.common.base.Predicate;

import net.minecraft.class_1105;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;

import net.legacyfabric.fabric.mixin.effect.StatusEffectStringsAccessor;

public class PotionHelperImpl {
	public static void registerPotionType(Item potionItem) {
		StatusEffectStringsAccessor.registerPotionType(new class_1105.C_96192416(potionItem));
	}

	public static void registerPotionType(Item potionItem, int meta) {
		StatusEffectStringsAccessor.registerPotionType(new class_1105.C_96192416(potionItem, meta));
	}

	public static void registerPotionTypeRecipe(PotionItem basePotion, Item ingredient, PotionItem resultingPotion) {
		StatusEffectStringsAccessor.registerPotionTypeRecipe(basePotion, new class_1105.C_96192416(ingredient), resultingPotion);
	}

	public static void registerPotionTypeRecipe(PotionItem basePotion, Item ingredient, int meta, PotionItem resultingPotion) {
		StatusEffectStringsAccessor.registerPotionTypeRecipe(basePotion, new class_1105.C_96192416(ingredient, meta), resultingPotion);
	}

	public static void registerPotionRecipe(Potion basePotion, Item ingredient, Potion resultingPotion) {
		StatusEffectStringsAccessor.registerPotionRecipe(basePotion, new class_1105.C_96192416(ingredient), resultingPotion);
	}

	public static void registerPotionRecipe(Potion basePotion, Item ingredient, int meta, Potion resultingPotion) {
		StatusEffectStringsAccessor.registerPotionRecipe(basePotion, new class_1105.C_96192416(ingredient, meta), resultingPotion);
	}

	public static void registerPotionRecipe(Potion basePotion, Predicate<ItemStack> ingredientPredicate, Potion resultingPotion) {
		StatusEffectStringsAccessor.registerPotionRecipe(basePotion, ingredientPredicate, resultingPotion);
	}
}
