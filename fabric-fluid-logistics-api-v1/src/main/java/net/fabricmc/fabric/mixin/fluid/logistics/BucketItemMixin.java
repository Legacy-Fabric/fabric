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
