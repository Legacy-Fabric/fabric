/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.fabricmc.fabric.test.object.builder;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.armor.v1.ArmorMaterial;
import net.fabricmc.fabric.api.content.registry.v1.BlockRegistry;
import net.fabricmc.fabric.api.content.registry.v1.ItemRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.ArmorMaterialBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockMaterial;
import net.fabricmc.fabric.impl.armor.EquipmentSlot;
import net.fabricmc.fabric.impl.armor.FabricArmorItem;

public class ObjectBuilderTest implements ModInitializer {
	public static final Material TEST_MATERIAL = new FabricBlockMaterial(MaterialColor.WHITE)
			.requiresSilkTouch()
			.setCanBeBrokenInAdventureMode()
			.doesNotBlockMovement()
			.noCollision()
			.setFlammable();
	public static final Block TEST_BLOCK = BlockRegistry.register(new Identifier("fabric-object-builder-api-v1-testmod:test_block"), new Block(TEST_MATERIAL, MaterialColor.AIR));
	public static final ArmorMaterial TEST_ARMOR_MATERIAL = new ArmorMaterialBuilder("test")
			.setDurabilityMultiplier(5)
			.setEnchantability(14)
			.setProtectionValues(new int[]{3, 4, 3, 2}).setRepairIngredient(Items.APPLE)
			.build();
	public static final Item TEST_ARMOR_ITEM = ItemRegistry.register(new Identifier("fabric-object-builder-api-v1-testmod:test_helmet"), new FabricArmorItem(TEST_ARMOR_MATERIAL, EquipmentSlot.HEAD).setItemGroup(ItemGroup.FOOD));

	@Override
	public void onInitialize() {
	}
}
