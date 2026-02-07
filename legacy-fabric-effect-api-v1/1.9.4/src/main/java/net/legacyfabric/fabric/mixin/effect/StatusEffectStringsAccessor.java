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

import net.minecraft.class_1105;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;

@Mixin(class_1105.class)
public interface StatusEffectStringsAccessor {
	@Invoker("m_71298295")
	static void registerPotionType(class_1105.C_96192416 potion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("m_10569388")
	static void registerPotionTypeRecipe(PotionItem basePotion, class_1105.C_96192416 ingredient, PotionItem resultingPotion) {
		throw new UnsupportedOperationException();
	}

	@Invoker("m_03472859")
	static void registerPotionRecipe(Potion basePotion, Predicate<ItemStack> ingredient, Potion resultingPotion) {
		throw new UnsupportedOperationException();
	}
}
