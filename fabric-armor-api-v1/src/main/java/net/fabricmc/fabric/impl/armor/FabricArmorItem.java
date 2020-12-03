/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.impl.armor;

import java.util.Random;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.armor.v1.ArmorMaterial;
import net.fabricmc.fabric.mixin.armor.MixinArmorItem;

public class FabricArmorItem extends ArmorItem {
	private final ArmorMaterial material;
	private final int slotId;

	public FabricArmorItem(ArmorMaterial material, EquipmentSlot slot) {
		super(/*Can be anything but null*/Material.DIAMOND, new Random().nextInt(16777216), slot.getSlotId());
		this.material = material;
		this.slotId = slot.getSlotId();
		((MixinArmorItem) this).setProtection(material.getProtectionValue(slot.getSlotId()));
		this.setMaxDamage(ArmorMaterial.BASE_DURABILITY[this.slotId] * material.getDurabilityMultiplier());
	}

	public ArmorMaterial getArmorMaterial() {
		return this.material;
	}

	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.getById(this.slotId);
	}

	@Override
	public int getEnchantability() {
		return this.material.getEnchantability();
	}

	@Override
	public int getMaxCount() {
		return 1;
	}

	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().test(ingredient);
	}

	@Deprecated
	@Override
	public int getDisplayColor(ItemStack itemStack, int i) {
		return i;
	}

	@Deprecated
	@Override
	public int getColor(ItemStack itemStack) {
		return 16777215;
	}

	@Deprecated
	@Override
	public void removeColor(ItemStack stack) {
	}

	@Deprecated
	@Override
	public void setColor(ItemStack itemStack, int i) {
	}

	@Deprecated
	@Override
	public boolean hasColor(ItemStack stack) {
		return false;
	}
}
