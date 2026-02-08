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

package net.legacyfabric.fabric.mixin.effect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.crafting.recipe.Ingredient;
import net.minecraft.item.Item;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.BrewingRecipes;
import net.minecraft.potion.Potion;

@Mixin(BrewingRecipes.class)
public interface StatusEffectStringsAccessor {
	@Invoker("registerBottleItem")
	static void registerPotionType(PotionItem potion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("registerBottleRecipe")
	static void registerPotionTypeRecipe(PotionItem basePotion, Item ingredient, PotionItem resultingPotion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("registerPotionRecipe")
	static void registerPotionRecipe(Potion basePotion, Item ingredient, Potion resultingPotion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("registerPotionRecipe")
	static void registerPotionRecipe(Potion basePotion, Ingredient ingredient, Potion resultingPotion) {
		throw new UnsupportedOperationException();
	}
}
