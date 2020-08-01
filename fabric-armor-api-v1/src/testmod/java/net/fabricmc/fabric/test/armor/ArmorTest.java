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

package net.fabricmc.fabric.test.armor;

import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.armor.v1.ArmorMaterial;
import net.fabricmc.fabric.api.content.registry.v1.ItemRegistry;
import net.fabricmc.fabric.impl.armor.EquipmentSlot;
import net.fabricmc.fabric.impl.armor.FabricArmorItem;

public class ArmorTest implements ModInitializer {
	private static final ArmorMaterial CUSTOM_ARMOR_MATERIAL = new ArmorMaterial() {
		@Override
		public int getEnchantability() {
			return 30;
		}

		@Override
		public Supplier<ItemStack> getRepairIngredient() {
			return () -> new ItemStack(Items.POTATO);
		}

		@Override
		public int getDurabilityMultiplier() {
			return 2;
		}

		@Override
		public String getName() {
			return "potato";
		}

		@Override
		public int getProtectionValue(int slot) {
			return 0;
		}
	};
	public static final Item POTATO_HELMET = ItemRegistry.register(new Identifier("fabric-armor-api-v1", "potato_helmet"), new FabricArmorItem(CUSTOM_ARMOR_MATERIAL, EquipmentSlot.HEAD));
	public static final Item POTATO_CHESTPLATE = ItemRegistry.register(new Identifier("fabric-armor-api-v1", "potato_helmet"), new FabricArmorItem(CUSTOM_ARMOR_MATERIAL, EquipmentSlot.CHEST));
	public static final Item POTATO_LEGGINGS = ItemRegistry.register(new Identifier("fabric-armor-api-v1", "potato_leggings"), new FabricArmorItem(CUSTOM_ARMOR_MATERIAL, EquipmentSlot.LEGS));
	public static final Item POTATO_BOOTS = ItemRegistry.register(new Identifier("fabric-armor-api-v1", "potato_boots"), new FabricArmorItem(CUSTOM_ARMOR_MATERIAL, EquipmentSlot.FEET));

	@Override
	public void onInitialize() {
	}
}
