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

package net.legacyfabric.fabric.mixin.effect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.effect.StatusEffectStrings;
import net.minecraft.item.Item;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;

@Mixin(StatusEffectStrings.class)
public interface StatusEffectStringsAccessor {
	@Invoker("method_14241")
	static void registerPotionType(PotionItem potion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("method_11419")
	static void registerPotionTypeRecipe(PotionItem basePotion, Item ingredient, PotionItem resultingPotion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("method_14242")
	static void registerPotionRecipe(Potion basePotion, Item ingredient, Potion resultingPotion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("method_14243")
	static void registerPotionRecipe(Potion basePotion, Ingredient ingredient, Potion resultingPotion) {
		throw new UnsupportedOperationException();
	}
}
