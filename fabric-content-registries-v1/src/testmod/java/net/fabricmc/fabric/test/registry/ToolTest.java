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

package net.fabricmc.fabric.test.registry;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.content.registry.v1.ItemRegistry;
import net.fabricmc.fabric.api.content.registry.v1.ToolMaterial;
import net.fabricmc.fabric.impl.content.registries.FabricPickaxeItem;

public class ToolTest implements ModInitializer {
	private static final FabricPickaxeItem PICKAXE = (FabricPickaxeItem) ItemRegistry.register(new Identifier("modid", "pig_iron_pickaxe"), new FabricPickaxeItem(3, new MinecartArmorMaterial()));

	@Override
	public void onInitialize() {
		//just loads the class
	}

	private static class MinecartArmorMaterial implements ToolMaterial {
		@Override
		public int getDurability() {
			return 500;
		}

		@Override
		public float getMiningSpeedMultiplier() {
			return 2.1F;
		}

		@Override
		public float getAttackMultiplier() {
			return 2.2F;
		}

		@Override
		public int getMiningLevel() {
			return 3;
		}

		@Override
		public int getEnchantability() {
			return 35;
		}

		@Override
		public Supplier<ItemStack> getRepairIngredient() {
			return () -> new ItemStack(Items.MINECART);
		}
	}
}
