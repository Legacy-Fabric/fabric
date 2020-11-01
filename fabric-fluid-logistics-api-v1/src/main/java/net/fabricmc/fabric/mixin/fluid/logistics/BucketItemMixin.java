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

package net.fabricmc.fabric.mixin.fluid.logistics;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.fluid.logistics.v1.FluidContainable;
import net.fabricmc.fabric.api.fluid.logistics.v1.FluidStack;
import net.fabricmc.fabric.api.fluid.logistics.v1.unit.FluidUnit;

@Mixin(BucketItem.class)
public class BucketItemMixin implements FluidContainable {
	@Shadow
	private Block fluid;

	@Override
	public FluidStack asFluidStack(ItemStack stack) {
		if (stack.getItem() instanceof BucketItem) {
			return new FluidStack((FluidBlock) this.fluid, 1, FluidUnit.BLOCK);
		}

		return FluidStack.EMPTY;
	}
}
