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

package net.fabricmc.fabric.mixin.content.registries;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.content.registry.v1.ItemStackHelper;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getEnchantability()I"), method = "calculateEnchantmentPower")
	private static int modifyEnchantmentPower(Item item, Random random, int i, int j, ItemStack stack) {
		return ItemStackHelper.getEnchantability(stack);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getEnchantability()I"), method = "getEnchantmentInfoEntries")
	private static int modifyInfoEntries(Item item, Random random, ItemStack stack, int i) {
		return ItemStackHelper.getEnchantability(stack);
	}
}
