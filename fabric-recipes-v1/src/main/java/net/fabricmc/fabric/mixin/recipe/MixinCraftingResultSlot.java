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

package net.fabricmc.fabric.mixin.recipe;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.container.CraftingResultSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.recipe.v1.FabricRecipeRemainder;
import net.fabricmc.fabric.api.recipe.v1.RecipeEvents;
import net.fabricmc.fabric.impl.base.util.ActionResult;

@Mixin(CraftingResultSlot.class)
public class MixinCraftingResultSlot {
	@Shadow
	@Final
	private CraftingInventory craftingInv;

	@Shadow
	@Final
	private PlayerEntity player;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V"), method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", cancellable = true)
	public void onCrafted(ItemStack stack, CallbackInfo ci) {
		ActionResult result = RecipeEvents.ITEM_CRAFTED.invoker().onCraft(stack, this.craftingInv, this.player);

		if (result == ActionResult.FAIL) {
			ci.cancel();
		}
	}

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/recipe/RecipeDispatcher;getRemainders(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)[Lnet/minecraft/item/ItemStack;"), method = "onTakeItem")
	public ItemStack[] modifyRemainders(ItemStack[] itemStacks) {
		for (int i = 0; i < this.craftingInv.getInvSize(); i++) {
			ItemStack invStack = this.craftingInv.getInvStack(i);

			if (invStack != null && invStack.getItem() instanceof FabricRecipeRemainder) {
				itemStacks[i] = ((FabricRecipeRemainder) invStack.getItem()).getRecipeRemainder(invStack.copy(), this.craftingInv, this.player);
			}
		}

		return itemStacks;
	}
}
