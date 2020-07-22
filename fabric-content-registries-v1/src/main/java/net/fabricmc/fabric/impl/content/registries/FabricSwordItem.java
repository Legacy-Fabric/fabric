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

package net.fabricmc.fabric.impl.content.registries;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.attribute.AttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.content.registry.v1.ToolMaterial;

public class FabricSwordItem extends Item {
	private final float attackMultiplier;
	private final ToolMaterial material;

	public FabricSwordItem(ToolMaterial material) {
		this.material = material;
		this.maxCount = 1;
		this.setMaxDamage(material.getDurability());
		this.setItemGroup(ItemGroup.COMBAT);
		this.attackMultiplier = 4.0F + material.getAttackMultiplier();
	}

	public float getAttackDamage() {
		return this.material.getAttackMultiplier();
	}

	public float getMiningSpeedForBlock(ItemStack stack, Block block) {
		if (block == Blocks.COBWEB) {
			return 15.0F;
		} else {
			Material material = block.getMaterial();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.SWORD && material != Material.FOILAGE && material != Material.PUMPKIN ? 1.0F : 1.5F;
		}
	}

	@Environment(EnvType.CLIENT)
	public boolean isHandheld() {
		return true;
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	public ItemStack method_8260(ItemStack stack, World world, PlayerEntity player) {
		player.method_7996(stack, this.getMaxUseTime(stack));
		return stack;
	}

	public boolean isEffectiveOn(Block block) {
		return block == Blocks.COBWEB;
	}

	public int getEnchantability() {
		return this.material.getEnchantability();
	}

	public String getToolMaterial() {
		return this.material.toString();
	}

	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().get().getItem() == ingredient.getItem();
	}

	public Multimap<String, AttributeModifier> getAttributeModifierMap() {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifierMap();
		multimap.put(EntityAttributes.ATTACK_DAMAGE.getId(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, "Weapon modifier", this.attackMultiplier, 0));
		return multimap;
	}
}
