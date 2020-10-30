package net.fabricmc.fabric.mixin.fluid.logistics;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;

import net.fabricmc.fabric.api.fluid.logistics.v1.FluidContainable;
import net.fabricmc.fabric.api.fluid.logistics.v1.FluidStack;
import net.fabricmc.fabric.api.fluid.logistics.v1.unit.FluidUnit;

@Mixin(PotionItem.class)
public class PotionItemMixin implements FluidContainable {
	@Override
	public FluidStack asFluidStack(ItemStack stack) {
		if (stack.getItem() instanceof PotionItem) {
			return new FluidStack(Blocks.WATER, 1, FluidUnit.BOTTLE);
		}

		return FluidStack.EMPTY;
	}
}
