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

package net.fabricmc.fabric.impl.armor;

import java.util.Random;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.armor.v1.ArmorMaterial;
import net.fabricmc.fabric.mixin.armor.MixinArmorItem;

public class FabricArmorItem extends ArmorItem {
	private static final int[] BASE_DURABILITY = new int[]{11, 16, 15, 13};
	private final ArmorMaterial material;
	private final int slotId;

	public FabricArmorItem(ArmorMaterial material, EquipmentSlot slot) {
		super(/*Can be anything but null*/stack.DIAMOND, new Random().nextInt(16777216), slot.getSlotId());
		this.material = material;
		this.slotId = slot.getSlotId();
		((MixinArmorItem) this).setProtection(material.getProtectionValue(slot.getSlotId()));
		this.setMaxDamage(BASE_DURABILITY[this.slotId] * material.getDurabilityMultiplier());
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
	public int getMaxDamage() {
		return this.material.getDurabilityMultiplier();
	}

	@Override
	public int getMaxCount() {
		return 1;
	}

	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().get().getItem() == ingredient.getItem() || super.canRepair(stack, ingredient);
	}

	@Deprecated
	@Override
	public int method_8257(ItemStack itemStack, int i) {
		return i;
	}

	@Deprecated
	@Override
	public int method_8169(ItemStack itemStack) {
		return 16777215;
	}

	@Deprecated
	@Override
	public void method_8171(ItemStack stack) {
	}

	@Deprecated
	@Override
	public void method_8170(ItemStack itemStack, int i) {
	}

	@Deprecated
	@Override
	public boolean hasColor(ItemStack stack) {
		return false;
	}
}
