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

import com.google.common.base.Predicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.effect.StatusEffectStrings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;

@Mixin(StatusEffectStrings.class)
public interface StatusEffectStringsAccessor {
	@Invoker("method_11421")
	static void registerPotionType(StatusEffectStrings.class_2696 potion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("method_11419")
	static void registerPotionTypeRecipe(PotionItem basePotion, StatusEffectStrings.class_2696 ingredient, PotionItem resultingPotion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("method_11420")
	static void registerPotionRecipe(Potion basePotion, Predicate<ItemStack> ingredient, Potion resultingPotion) {
		throw new UnsupportedOperationException();
	}
}
