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

package net.fabricmc.fabric.mixin.content.registries;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.content.registry.v1.ItemStackHelper;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/Item;getEnchantability()I"), method = "calculateEnchantmentPower", name = "k", index = 1)
	private static int modifyK(int k, Random random, int i, int j, ItemStack itemStack) {
		return ItemStackHelper.getEnchantability(itemStack);
	}

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/Item;getEnchantability()I"), method = "getEnchantmentInfoEntries", name = "j")
	private static int modifyJ(int j, Random random, ItemStack itemStack, int i) {
		return ItemStackHelper.getEnchantability(itemStack);
	}
}
