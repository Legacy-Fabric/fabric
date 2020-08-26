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

package net.fabricmc.fabric.api.object.builder.v1.block;

import java.util.function.Predicate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.armor.v1.ArmorMaterial;

public final class ArmorMaterialBuilder {
	private final String name;
	private int enchantability = 10;
	private int durabilityMultiplier = 15;
	private int[] protectionValues = new int[]{2, 5, 4, 1};
	private Predicate<ItemStack> repairIngredientPredicate = itemStack -> true;

	public ArmorMaterialBuilder(String name) {
		this.name = name;
	}

	public ArmorMaterialBuilder setDurabilityMultiplier(int durabilityMultiplier) {
		this.durabilityMultiplier = durabilityMultiplier;
		return this;
	}

	public ArmorMaterialBuilder setEnchantability(int enchantability) {
		this.enchantability = enchantability;
		return this;
	}

	public ArmorMaterialBuilder setProtectionValues(int[] protectionValues) {
		this.protectionValues = protectionValues;
		return this;
	}

	public ArmorMaterialBuilder setRepairIngredient(Predicate<ItemStack> repairIngredientPredicate) {
		this.repairIngredientPredicate = repairIngredientPredicate;
		return this;
	}

	public ArmorMaterialBuilder setRepairIngredient(Item repairIngredient) {
		this.repairIngredientPredicate = itemStack -> itemStack.getItem() == repairIngredient;
		return this;
	}

	public ArmorMaterial build() {
		return new ArmorMaterial() {
			@Override
			public String getName() {
				return ArmorMaterialBuilder.this.name;
			}

			@Override
			public int getEnchantability() {
				return ArmorMaterialBuilder.this.enchantability;
			}

			@Override
			public Predicate<ItemStack> getRepairIngredient() {
				return ArmorMaterialBuilder.this.repairIngredientPredicate;
			}

			@Override
			public int getDurabilityMultiplier() {
				return ArmorMaterialBuilder.this.durabilityMultiplier;
			}

			@Override
			public int getProtectionValue(int slot) {
				return ArmorMaterialBuilder.this.protectionValues[slot];
			}
		};
	}
}
